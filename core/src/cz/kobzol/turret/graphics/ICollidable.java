package cz.kobzol.turret.graphics;

import com.badlogic.gdx.math.Rectangle;

/**
 * Represents objects that can collide with other objects.
 */
public interface ICollidable {
    /**
     * Determines if the given object collides with another object.
     * @param collidable collidable object
     * @return True if the objects collide, false otherwise
     */
    boolean collidesWith(ICollidable collidable);

    /**
     * Gets the bounding box of the object.
     * @return bounding box
     */
    Rectangle getBoundingBox();
}
