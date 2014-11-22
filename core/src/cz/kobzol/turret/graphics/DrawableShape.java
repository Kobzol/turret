package cz.kobzol.turret.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IPositionable;
import cz.kobzol.turret.model.GameObject;

import java.awt.Dimension;

/**
 * Represents object's with shape and texture that can graphics themselves.
 */
public abstract class DrawableShape extends GameObject implements IDrawable, IPositionable {
    protected Texture texture;
    protected Dimension dimension;
    protected Vector2 position;

    public DrawableShape() {
        this.dimension = new Dimension();
        this.position = new Vector2(0, 0);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    @Override
    public Dimension getDimension() {
        return new Dimension(this.dimension);
    }

    @Override
    public void setDimension(Dimension dimension) {
        this.dimension.setSize(dimension.getSize());
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(this.position);
    }

    @Override
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }
}
