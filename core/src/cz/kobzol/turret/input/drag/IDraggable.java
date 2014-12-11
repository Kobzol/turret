package cz.kobzol.turret.input.drag;

import cz.kobzol.turret.graphics.ICollidable;
import cz.kobzol.turret.graphics.IPositionable;

/**
 * Represents draggable objects.
 */
public interface IDraggable extends IPositionable, ICollidable {
    DragContainer getDragContainer();
    boolean isDraggable();
}
