package cz.kobzol.turret.model.field;

import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.util.Collections;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Searches for the best path in a graph.
 */
public class PathFinder {
    private static List<FieldSlot> findShortestPath(Field field, FieldSlot start, FieldSlot end, int[][] directions) {
        Dimension fieldDimension = field.getFieldDimension();
        int slotCount = fieldDimension.width * fieldDimension.height;

        boolean[] visited = new boolean[slotCount];
        int[] predecessors = new int[slotCount];
        int[] distances = new int[slotCount];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        distances[start.getIndex()] = 0;

        LinkedList<FieldSlot> queue = new LinkedList<FieldSlot>();
        queue.add(start);

        while (queue.size() > 0) {
            FieldSlot slot = queue.poll();

            visited[slot.getIndex()] = true;

            for (int[] shift : directions) {
                int newX = (int) slot.getPosition().x + shift[0];
                int newY = (int) slot.getPosition().y + shift[1];

                if (field.containsIndex(newX, newY)) {
                    Vector2 index = new Vector2(newX, newY);
                    FieldSlot incident = field.getSlotForIndex(index);

                    if (!visited[incident.getIndex()] && !incident.isPlatform()) {
                        visited[incident.getIndex()] = true;
                        predecessors[incident.getIndex()] = slot.getIndex();
                        distances[incident.getIndex()] = distances[slot.getIndex()] + 1;

                        if (incident == end) {
                            break;
                        }
                        else queue.add(incident);
                    }
                }
            }
        }

        if (distances[end.getIndex()] == Integer.MAX_VALUE) { // the path to the end was not found
            return null;
        }
        else {
            List<FieldSlot> path = new ArrayList<FieldSlot>();

            int predecessor = end.getIndex();

            while (predecessor != start.getIndex()) {
                path.add(field.getSlotForIndex(predecessor));
                predecessor = predecessors[predecessor];
            }

            path.add(start);    // add the first slot to the path

            java.util.Collections.reverse(path); // reverse the path

            return path;
        }
    }
    public static List<FieldSlot> findShortestPath(Field field, FieldSlot start, FieldSlot end) {
        return PathFinder.findShortestPath(field, start, end, new int[][]{
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        });
    }
    public static List<FieldSlot> findShortestWeightedPath(Field field, FieldSlot start, FieldSlot end, int[][] directions) {
        Dimension fieldDimension = field.getFieldDimension();
        int slotCount = fieldDimension.width * fieldDimension.height;

        boolean[] visited = new boolean[slotCount];
        int[] predecessors = new int[slotCount];
        final int[] distances = new int[slotCount];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        distances[start.getIndex()] = 0;

        PriorityQueue<FieldSlot> queue = new PriorityQueue<FieldSlot>(slotCount, new Comparator<FieldSlot>() {
            @Override
            public int compare(FieldSlot lhs, FieldSlot rhs) {
                if (distances[lhs.getIndex()] < distances[rhs.getIndex()]) {
                    return -1;
                }
                else return 1;
            }
        });
        queue.add(start);

        while (queue.size() > 0) {
            FieldSlot slot = queue.poll();

            visited[slot.getIndex()] = true;

            for (int[] shift : directions) {
                int newX = (int) slot.getPosition().x + shift[0];
                int newY = (int) slot.getPosition().y + shift[1];

                if (field.containsIndex(newX, newY)) {
                    Vector2 index = new Vector2(newX, newY);
                    FieldSlot incident = field.getSlotForIndex(index);

                    if (!visited[incident.getIndex()] &&
                        !incident.isPlatform() &&
                        distances[slot.getIndex()] + incident.getCost() < distances[incident.getIndex()]) {
                            predecessors[incident.getIndex()] = slot.getIndex();
                            distances[incident.getIndex()] = distances[slot.getIndex()] + incident.getCost();

                            if (incident == end) {
                                break;
                            }
                            else
                            {
                                queue.remove(incident); // update it's position in the priority queue
                                queue.add(incident);
                            }
                    }
                }
            }
        }

        if (distances[end.getIndex()] == Integer.MAX_VALUE) { // the path to the end was not found
            return null;
        }
        else {
            List<FieldSlot> path = new ArrayList<FieldSlot>();

            int predecessor = end.getIndex();

            while (predecessor != start.getIndex()) {
                path.add(field.getSlotForIndex(predecessor));
                predecessor = predecessors[predecessor];
            }

            path.add(start);    // add the first slot to the path

            java.util.Collections.reverse(path); // reverse the path

            return path;
        }
    }

    private List<FieldSlot> waypoints = new ArrayList<FieldSlot>();
    private int waypointID = 0;
    private int directions[][] = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };
    private int turretCount = 0;

    public PathFinder() {
        Collections.shuffleArray(this.directions);
    }

    public boolean isPathValid(Field field) {
        if (this.waypoints.size() == 0) { // the path hasn't been found yet
            return false;
        }

        if (this.turretCount != field.getTurretCount()) { // some turrets were added
            return false;
        }

        for (int i = this.waypointID; i < this.waypoints.size(); i++) {
            if (this.waypoints.get(i).isPlatform()) {
                return false;
            }
        }

        return true;
    }

    public List<FieldSlot> findPath(Field field) {
        List<FieldSlot> path = null;

        if (this.waypoints.size() == 0) {
            path = PathFinder.findShortestWeightedPath(field, field.getStartSlot(), field.getEndSlot(), this.directions);
        }
        else path = PathFinder.findShortestWeightedPath(field, this.waypoints.get(this.waypointID), field.getEndSlot(), this.directions);

        this.turretCount = field.getTurretCount(); // path updated, we know about all the turrets

        return path;
    }

    public void setPath(List<FieldSlot> path) {
        this.waypoints = path;
        this.waypointID = 0;
    }

    public FieldSlot getNextTarget() {
        if (this.waypointID + 1 < this.waypoints.size()) {
            return this.waypoints.get(this.waypointID + 1);
        }
        else return this.waypoints.get(this.waypoints.size() - 1);
    }
    public void advanceInPath() {
        this.waypointID++;
    }
}
