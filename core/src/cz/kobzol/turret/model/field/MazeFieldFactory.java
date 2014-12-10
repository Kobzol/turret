package cz.kobzol.turret.model.field;

import com.badlogic.gdx.math.Vector2;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Field factory that creates mazes.
 */
public class MazeFieldFactory {
    private Dimension fieldDimension;
    private Dimension slotDimension;
    private Vector2 startIndex;
    private Vector2 endIndex;

    public MazeFieldFactory(Dimension fieldDimension, Dimension slotDimension, Vector2 startIndex, Vector2 endIndex) {
        this.fieldDimension = fieldDimension;
        this.slotDimension = slotDimension;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public Field createMaze() {
        return this.generateMazeField(this.fieldDimension, this.slotDimension, this.startIndex, this.endIndex);
    }

    private boolean isInsideField(Field field, int x, int y) {
        return  x >= 0 && x < field.getFieldDimension().width &&
                y >= 0 && y < field.getFieldDimension().height;
    }

    public Field generateMazeField(Dimension dimension, Dimension slotDimension, Vector2 startIndex, Vector2 endIndex) {
        startIndex.scl(2);  // ignore edges
        endIndex.scl(2);

        Field field = new Field(new Dimension(dimension.width * 2 - 1, dimension.height * 2 - 1), new Dimension(slotDimension), startIndex, endIndex);
        Random random = new Random();
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        int[] predecessors = new int[field.getFieldDimension().width * field.getFieldDimension().height];
        Arrays.fill(predecessors, -1);

        PriorityQueue<FieldSlot> queue = new PriorityQueue<FieldSlot>(dimension.width * dimension.height, new Comparator<FieldSlot>() {
            @Override
            public int compare(FieldSlot lhs, FieldSlot rhs) {
                if (lhs.getCost() < rhs.getCost()) {
                    return -1;
                }
                else return 1;
            }
        });

        // random edge weight allocation
        for (int y = 0; y < field.getFieldDimension().height; y++) {
            for (int x = 0; x < field.getFieldDimension().width; x++) {
                field.getSlotForIndex(new Vector2(x, y)).setPlatform(true);

                if (x % 2 == 0 && y % 2 == 0) {
                    for (int[] point : directions) {
                        int newX = x + point[0];
                        int newY = y + point[1];

                        if (this.isInsideField(field, newX, newY)) {
                            field.getSlotForIndex(new Vector2(newX, newY)).setCost(random.nextInt(10));
                        }
                    }

                    FieldSlot slot = field.getSlotForIndex(new Vector2(x, y));
                    slot.setPlatform(false);

                    if (x == startIndex.x && y == startIndex.y) {
                        slot.setCost(0);
                    }
                    else slot.setCost(Integer.MAX_VALUE);

                    queue.add(slot);
                }
                }
        }

        while (!queue.isEmpty()) {
            FieldSlot slot = queue.poll();

            for (int[] point : directions) {
                Vector2 neighborPos = new Vector2((int) slot.getPosition().x + point[0] * 2, (int) slot.getPosition().y + point[1] * 2);

                if (this.isInsideField(field, (int) neighborPos.x, (int) neighborPos.y)) {
                    FieldSlot neighbor = field.getSlotForIndex(neighborPos);
                    FieldSlot edge = field.getSlotForIndex(new Vector2((int) slot.getPosition().x + point[0], (int) slot.getPosition().y + point[1]));

                    if (queue.contains(neighbor) && edge.getCost() < neighbor.getCost()) {
                        predecessors[neighbor.getIndex()] = slot.getIndex();
                        neighbor.setCost(edge.getCost());
                        queue.remove(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        for (int i = 0; i < predecessors.length; i++) {
            if (predecessors[i] == -1) {
                continue;
            }

            FieldSlot current = field.getSlotForIndex(i);
            FieldSlot predecessor = field.getSlotForIndex(predecessors[i]);

            Vector2 edgePosition = current.getPosition().add(predecessor.getPosition()).scl(0.5f);
            FieldSlot edge = field.getSlotForIndex(edgePosition);
            edge.setPlatform(false);
        }

        field.resetCosts();

        // make more random ways
        for (int y = 0; y < field.getFieldDimension().height; y++) {
            for (int x = (y % 2 == 0 ? 1 : 0); x < field.getFieldDimension().width; x++) {
                if (random.nextFloat() < 0.1f) {
                    field.getSlotForIndex(new Vector2(x, y)).setPlatform(false);
                }
            }
        }

        field.getSlotForIndex(startIndex).setPlatform(false);
        field.getSlotForIndex(endIndex).setPlatform(false);

        return field;
    }

}
