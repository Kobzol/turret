package cz.kobzol.turret.graphics;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents objects that can move.
 */
public interface IMovable {
    /**
     * Moves the object in it's direction with it's speed.
     */
    void move(float delta);

    Vector2 getMoveDirection();
    void setMoveDirection(Vector2 moveDirection);

    float getSpeed();
    void setSpeed(float speed);
}
