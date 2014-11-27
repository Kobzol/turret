package cz.kobzol.turret.input.drag;

/**
 * Represents objects that can be receive other objects through drop.
 */
public interface IDroppable {
    boolean receiveObject(IDraggable draggableObject);
}
