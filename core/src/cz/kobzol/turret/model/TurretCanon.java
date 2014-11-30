package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.util.Cooldown;

/**
 * Turret canon.
 */
public class TurretCanon extends SpriteObject {
    protected float range;
    protected float damage;

    protected Cooldown fire_cooldown;

    public TurretCanon(float range, float damage, long fire_delay) {
        this.range = range;
        this.damage = damage;
        this.fire_cooldown = new Cooldown(fire_delay);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        this.fire_cooldown.update(delta);
    }
}
