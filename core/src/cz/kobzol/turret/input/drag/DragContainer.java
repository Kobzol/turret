package cz.kobzol.turret.input.drag;

import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IPositionable;

/**
 * Represents the draggable state of an object.
 */
public class DragContainer {
    public enum DragState {
        WAITING,
        BEING_DRAGGED
    }

    private final IPositionable draggedObject;
    private Vector2 initialPosition;
    private DragState dragState;

    public DragContainer(IPositionable draggedObject) {
        this.draggedObject = draggedObject;
        this.dragState = dragState;

        this.updateInitialPosition();
    }

    public IPositionable getDraggedObject() {
        return this.draggedObject;
    }

    public Vector2 getInitialPosition() {
        return this.initialPosition;
    }

    public final void updateInitialPosition() {
        this.initialPosition = this.draggedObject.getPosition();
    }

    public DragState getDragState() {
        return this.dragState;
    }
    public void startDrag() {
        this.dragState = DragState.BEING_DRAGGED;
    }
    public void stopDrag() {
        this.dragState = DragState.WAITING;
    }
}
