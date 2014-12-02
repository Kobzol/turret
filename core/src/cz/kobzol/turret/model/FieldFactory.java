package cz.kobzol.turret.model;

import com.badlogic.gdx.math.Vector2;

import java.awt.Dimension;

/**
 * Creates fields from text files.
 */
public class FieldFactory {
    private Dimension fieldDimension;
    private Dimension slotDimension;
    private Vector2 startIndex;
    private Vector2 endIndex;
    private boolean[][] slots;

    public FieldFactory(String fieldRepresentation) {
        this.createRepresentation(fieldRepresentation);
    }

    private void createRepresentation(String fieldRepresentation) {
        String[] lines = fieldRepresentation.split(System.getProperty("line.separator"));

        String[] fieldDims = lines[0].split("x");
        this.fieldDimension = new Dimension(Integer.parseInt(fieldDims[0]), (Integer.parseInt(fieldDims[1])));

        String[] slotDims = lines[1].split("x");
        this.slotDimension = new Dimension(Integer.parseInt(slotDims[0]), (Integer.parseInt(slotDims[1])));

        this.slots = new boolean[this.fieldDimension.height][this.fieldDimension.width];

        String[] startIndex = lines[2].split("x");
        this.startIndex = new Vector2(Integer.parseInt(startIndex[0]), this.fieldDimension.height - Integer.parseInt(startIndex[1]) - 1);

        String[] endIndex = lines[3].split("x");
        this.endIndex = new Vector2(Integer.parseInt(endIndex[0]), this.fieldDimension.height - Integer.parseInt(endIndex[1]) - 1);

        int offset = 3 + this.fieldDimension.height;
        for (int y = 0; y < this.fieldDimension.height; y++) {
            for (int x = 0; x < this.fieldDimension.width; x++) {
                int height = offset - y;
                this.slots[y][x] = lines[height].charAt(x) == '1';
            }
        }
    }

    public Field createField() {
        Field field = new Field(new Dimension(this.fieldDimension), new Dimension(this.slotDimension), this.startIndex, this.endIndex);

        for (int y = 0; y < fieldDimension.height; y++) {
            for (int x = 0; x < fieldDimension.width; x++) {
                field.getSlotForIndex(y * fieldDimension.width + x).setPlatform(this.slots[y][x]);
            }
        }

        return field;
    }
}
