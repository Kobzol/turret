package cz.kobzol.turret.model.turret;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.demon.Demon;
import cz.kobzol.turret.util.Cooldown;

import java.util.List;

/**
 * Turret canon.
 */
public abstract class TurretCanon extends SpriteObject {
    protected float range;
    protected float damage;

    protected Demon target;

    protected Cooldown fire_cooldown;

    public TurretCanon(float range, float damage, long fire_delay) {
        this.range = range;
        this.damage = damage;
        this.fire_cooldown = new Cooldown(fire_delay);
    }

    public void handleDemons(List<Turret> turrets, List<Demon> demons, float delta) {
        super.update(delta);

        this.fire_cooldown.update(delta);
    }

    public float getRange() {
        return this.range;
    }

    protected boolean isInRange(Demon demon) {
        return demon.getPosition().dst(this.getPosition()) <= this.range;
    }

    protected Demon findTarget(List<Demon> demons) {
        for (Demon demon : demons) {
            if (this.isInRange(demon)) {
                return demon;
            }
        }

        return null;
    }
}
