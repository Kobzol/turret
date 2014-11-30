package cz.kobzol.turret.input.click;

import com.badlogic.gdx.math.Rectangle;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Handles object dragging.
 */
public class Clicker {
    public void handleClick(IClickable clickableObject, MouseState mouseState) {
        ClickContainer container = clickableObject.getClickContainer();
        Rectangle boundingBox = clickableObject.getBoundingBox();

        if (mouseState.isPressed()) {
            if (boundingBox.contains(mouseState.getMousePosition())) {
                if (container.getClickState().equals(ClickContainer.ClickState.READY_TO_CLICK)) {
                    container.setMouseHold();
                }
            }
        }
        else {
            if (boundingBox.contains(mouseState.getMousePosition())) {
                if (container.getClickState().equals(ClickContainer.ClickState.MOUSE_HOLD) && clickableObject.isClickable()) {
                    container.performClick();
                }
                else if (container.getClickState().equals(ClickContainer.ClickState.WAITING)) {
                    container.setReadyToClick();
                }
            }
            else container.setWaiting();
        }
    }
}
