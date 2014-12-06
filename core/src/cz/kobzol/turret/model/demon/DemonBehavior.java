package cz.kobzol.turret.model.demon;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.model.screen.GameScreen;

/**
 * Represents behavior of a demon.
 */
public abstract class DemonBehavior implements Cloneable {
    protected DemonBehavior decorator;

    public DemonBehavior() {
        this(null);
    }

    public DemonBehavior(DemonBehavior demonBehavior) {
        this.decorator = demonBehavior;
    }

    public void render(Demon demon, Batch batch, Camera camera) {
        if (this.decorator != null) {
            this.decorator.render(demon, batch, camera);
        }
    }

    public void renderShape(Demon demon, ShapeRenderer shapeRenderer, Camera camera) {
        if (this.decorator != null) {
            this.decorator.renderShape(demon, shapeRenderer, camera);
        }
    }

    public void update(GameScreen gameScreen, Demon demon, float delta) {
        if (this.decorator != null) {
            this.decorator.update(gameScreen, demon, delta);
        }
    }

    @Override
    public Object clone() {
        try {
            DemonBehavior behavior = (DemonBehavior) super.clone();

            if (this.decorator != null) {
                behavior.decorator = (DemonBehavior) this.decorator.clone();
            }

            return behavior;
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
