package cz.kobzol.turret.graphics;

/**
 * Represents objects that can move.
 */
public interface IMovable {
    /**
     * Moves the object in it's direction with it's speed.
     */
    void move();

    float getSpeed();
    void setSpeed(float speed);
}
