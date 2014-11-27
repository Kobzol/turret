package cz.kobzol.turret.input.drag;

import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Handles object dragging.
 */
public class Dragger {
    public void handleDrag(IDraggable draggableObject, MouseState mouseState, IDroppable dropObject) {
        DragContainer container = draggableObject.getDragContainer();
        System.out.println(container.getDragState().toString());

        if (mouseState.isPressed()) {
            if (container.getDragState().equals(DragContainer.DragState.BEING_DRAGGED)) {
                container.moveObjectTo(mouseState.getMousePosition());
            }
            else if (draggableObject.getBoundingBox().contains(mouseState.getMousePosition()) && container.getDragState().equals(DragContainer.DragState.READY_TO_DRAG)) {
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
