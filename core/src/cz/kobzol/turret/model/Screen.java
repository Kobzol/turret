package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Represents a single game screen.
 */
public abstract class Screen implements IDrawable, IUpdatable {
    public Screen() {

    }

    @Override
    public abstract void render(Batch batch, Camera camera);

    @Override
    public abstract void renderShape(ShapeRenderer shapeRenderer, Camera camera);

    @Override
    public abstract void update(float delta);

    public abstract void handleInput(MouseState mouseState);
}
