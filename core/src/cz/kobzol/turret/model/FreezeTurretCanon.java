package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Texture;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Canon that freezes demons.
 */
public class FreezeTurretCanon extends LaserTurretCanon {
    protected class FreezeBullet extends Bullet {
        public FreezeBullet(float damage, LaserTurretCanon canon) {
            super(damage, canon);
        }

        protected void dealDamage(Demon demon) {
            demon.receiveDamage(this.getDamage() * 2.0f);
        }
    }

    public FreezeTurretCanon(float range, float damage, long fire_delay) {
        super(range, damage, fire_delay);
    }

    protected Bullet createTemplateBullet() {
        Bullet bullet = new FreezeBullet(this.damage, this);
        bullet.setSpeed(400.0f);
        bullet.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_BULLET_IMG));
        bullet.setDimension(new Dimension(8, 12));

        return bullet;
    }
}
