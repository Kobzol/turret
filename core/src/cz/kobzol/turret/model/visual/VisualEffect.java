package cz.kobzol.turret.model.visual;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.util.Cooldown;
import cz.kobzol.turret.util.IObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents visual effects.
 */
public abstract class VisualEffect implements IUpdatable, IDrawable, IObservable {
    private List<ObservableListener> listeners = new ArrayList<ObservableListener>();
    private final Cooldown cooldown;

    public VisualEffect(long duration) {
        this.cooldown = new Cooldown(duration);
    }

    @Override
    public void render(Batch batch, Camera camera) {

    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {

    }

    @Override
    public void addListener(ObservableListener listener) {
        this.listeners.add(listener);
    }

    private void notifyRemove() {
        for (ObservableListener listener : this.listeners) {
            listener.onRemove(this);
        }
    }

    protected void resetEffect() {

    }

    @Override
    public void update(float delta) {
        this.cooldown.update(delta);

        if (this.cooldown.isReady()) {
            this.resetEffect();
            this.notifyRemove();
        }
    }
}
