package cz.kobzol.turret.model.effect;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.Demon;
import cz.kobzol.turret.util.Cooldown;

/**
 * Represents damage over time.
 */
public class DamageOverTimeEffect extends Effect {
    private final int ticks;
    private final float tickDamage;
    private final Cooldown tickCooldown;

    public DamageOverTimeEffect(long duration_ms, int ticks, float tickDamage) {
        super(duration_ms);

        this.ticks = ticks;
        this.tickDamage = tickDamage;
        this.tickCooldown = new Cooldown(duration_ms / ticks);
    }

    @Override
    public void apply(SpriteObject object) {
        if (this.tickCooldown.resetIfReady()) {
            Demon demon = (Demon) object;
            demon.receiveDamage(this.tickDamage);
        }
    }

    @Override
    public void restore(SpriteObject object) {

    }

    @Override
    public boolean update(float delta) {
        this.tickCooldown.update(delta);

        return super.update(delta);
    }

    @Override
    public boolean stackWith(Effect effect) {
        return true;
    }
}
