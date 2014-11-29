package cz.kobzol.turret.input.click;

import cz.kobzol.turret.input.drag.IDroppable;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Handles object dragging.
 */
public class Clicker {
    public void handleClick(IClickable clickableObject, MouseState mouseState, IDroppable dropObject) {
        ClickContainer container = clickableObject.getClickContainer();

        if (mouseState.isPressed()) {
            if (clickableObject.getBoundingBox().contains(mouseState.getMousePosition())) {
                if (container.getClickState().equals(ClickContainer.ClickState.READY_TO_CLICK)) {
                    container.setMouseHold();
                }
            }
        }
        else {
            if (clickableObject.getBoundingBox().contains(mouseState.getMousePosition())) {
                if (container.getClickState().equals(ClickContainer.ClickState.READY_TO_CLICK) && clickableObject.isClickable()) {
                    container.performClick();
                }
                else container.setReadyToClick();
            }
            else container.setWaiting();
        }
    }
}
