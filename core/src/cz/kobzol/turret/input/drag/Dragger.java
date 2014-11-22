package cz.kobzol.turret.input.drag;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Handles object dragging.
 */
public class Dragger {
    public void handleDrag(IDraggable draggableObject, MouseState mouseState) {
        if (draggableObject.getBoundingBox().contains(mouseState.getMousePosition())) {

        }
    }
}
