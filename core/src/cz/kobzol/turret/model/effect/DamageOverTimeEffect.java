package cz.kobzol.turret.model.effect;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.demon.Demon;
import cz.kobzol.turret.util.Cooldown;

/**
 * Represents damage over time.
 */
public class DamageOverTimeEffect extends Effect {
    private int ticks;
    private float tickDamage;
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

    private float getTotalDamage() {
        return this.ticks * this.tickDamage;
    }

    @Override
    public boolean stackWith(Effect effect) {
        if (effect instanceof DamageOverTimeEffect) {
            DamageOverTimeEffect dot = (DamageOverTimeEffect) effect;

            if (dot.getTotalDamage() > this.getTotalDamage()) {
                this.tickDamage = dot.tickDamage;
                this.tickCooldown.setDuration(dot.getDuration() / dot.ticks);
                this.ticks = dot.ticks;
                this.setDuration(dot.getDuration());

                return true;
            }

            return false;
        }
        else return false;
    }
}
