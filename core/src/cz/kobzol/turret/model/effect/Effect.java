package cz.kobzol.turret.model.effect;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.util.Cooldown;

/**
 * Marks an effect.
 */
public abstract class Effect implements Cloneable {
    protected Cooldown duration;

    public Effect(long duration_ms) {
        this.duration = new Cooldown(duration_ms);
    }

    /**
     * Updates the effect.
     * @param delta delta time in ms
     * @return True, if the effect expired
     */
    public boolean update(float delta) {
        this.duration.update(delta);

        return this.duration.isReady();
    }

    public void refresh() {
        this.duration.reset();
    }

    public abstract void apply(SpriteObject object);
    public abstract void restore(SpriteObject object);
    public abstract boolean stack();
}
