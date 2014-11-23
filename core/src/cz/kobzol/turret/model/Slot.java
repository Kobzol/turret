package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Slot that contains other game objects.
 */
public class Slot extends SpriteObject {
    private final Stack<GameObject> containedObjects = new Stack<GameObject>();

    public boolean isEmpty() {
        return this.containedObjects.isEmpty();
    }

    public void addObject(GameObject object) {
        this.containedObjects.add(object);
    }

    public void removeObject() {
        assert(!this.isEmpty());

        this.containedObjects.pop();
    }
}
