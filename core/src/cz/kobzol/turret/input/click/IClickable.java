package cz.kobzol.turret.input.click;

import cz.kobzol.turret.graphics.ICollidable;
import cz.kobzol.turret.graphics.IPositionable;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Represents clickable objects.
 */
public interface IClickable extends IPositionable, ICollidable {
    ClickContainer getClickContainer();
    boolean isClickable();
    void onClick(MouseState mouseState);
}
