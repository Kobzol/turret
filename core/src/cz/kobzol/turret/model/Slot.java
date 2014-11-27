package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.input.drag.IDroppable;

import java.util.Stack;

/**
 * Slot that contains other game objects.
 */
public class Slot extends SpriteObject implements IDroppable {
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

    @Override
    public boolean receiveObject(IDraggable draggableObject) {
        draggableObject.setPosition(this.getPosition());
        this.containedObjects.add((GameObject) draggableObject);

        return true;
    }
}
