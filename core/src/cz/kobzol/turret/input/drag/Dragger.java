package cz.kobzol.turret.input.drag;

import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Handles object dragging.
 */
public class Dragger {
    public void handleDrag(IDraggable draggableObject, MouseState mouseState, IDroppable dropObject) {
        DragContainer container = draggableObject.getDragContainer();

        if (mouseState.isPressed()) {
            if (container.getDragState().equals(DragContainer.DragState.BEING_DRAGGED)) {
                if (!draggableObject.isDraggable()) {
                    container.revertDrag();
                }
                else container.moveObjectTo(mouseState.getMousePosition());
            }
            else if (draggableObject.getBoundingBox().contains(mouseState.getMousePosition()) &&
                     container.getDragState().equals(DragContainer.DragState.READY_TO_DRAG) &&
                     draggableObject.isDraggable()) {
                container.startDrag();
            }
        }
        else {
            if (container.getDragState().equals(DragContainer.DragState.BEING_DRAGGED)) {
                if (dropObject.receiveObject(draggableObject)) {
                    container.finalizeDrag();
                }
                else container.revertDrag();
            }
            else if (draggableObject.getBoundingBox().contains(mouseState.getMousePosition())) {
                container.setReadyToDrag();
            }
            else container.resetToWait();
        }
    }
}
