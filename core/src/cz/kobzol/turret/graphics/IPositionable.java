package cz.kobzol.turret.graphics;

import com.badlogic.gdx.math.Vector2;

import java.awt.Dimension;

/**
 * Represents objects with position.
 */
public interface IPositionable {
    /**
     * Get's the object's position.
     * @return position
     */
    Vector2 getPosition();

    /**
     * Sets the object's position.
     * @param position position
     */
    void setPosition(Vector2 position);

    /**
     * Set's the object's dimension.
     * @param dimension dimension
     */
    void setDimension(Dimension dimension);

    /**
     * Get's the object's dimension.
     * @return dimension of the object
     */
    Dimension getDimension();
}
