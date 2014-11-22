package cz.kobzol.turret.input.mouse;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents mouse state.
 */
public class MouseState {
    private final Vector2 mousePosition;
    private final boolean pressed;

    public MouseState(float x, float y, boolean pressed) {
        this.mousePosition = new Vector2(x, y);
        this.pressed = pressed;
    }

    public Vector2 getMousePosition() {
        return this.mousePosition;
    }
    public boolean isPressed() {
        return this.pressed;
    }
}
