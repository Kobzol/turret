package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import cz.kobzol.turret.model.demon.Demon;
import cz.kobzol.turret.model.effect.DamageOverTimeEffect;
import cz.kobzol.turret.model.effect.SpriteEffect;
import cz.kobzol.turret.model.effect.VelocityEffect;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Class for creating turret types.
 */
public class TurretFactory {
    private static int TURRET_SIZE = 30;

    public Turret createBulletTurret() {
        TurretCanon canon = new MissileTurretCanon(200, 50, 100, 300, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_CANON_IMG));
        canon.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));
        canon.setOrigin(15, 8);

        Turret turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_IMG));
        turret.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));

        return turret;
    }

    public Turret createFreezeTurret() {
        TurretCanon canon = new MissileTurretCanon(200, 50, 500, 200, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
                demon.addEffect(new VelocityEffect(1000, 0.5f));
                demon.addEffect(new SpriteEffect(1000, new Color(0, 0, 1, 0.5f)));
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_CANON_IMG));
        canon.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));
        canon.setOrigin(15, 8);

        Turret turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_IMG));
        turret.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));

        return turret;
    }

    public Turret createPoisonTurret() {
        TurretCanon canon = new MissileTurretCanon(200, 50, 500, 200, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
                demon.addEffect(new DamageOverTimeEffect(1000, 3, TurretFactory.TURRET_SIZE));
                demon.addEffect(new SpriteEffect(1000, new Color(0, 1, 0, 0.8f)));
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET3_CANON_IMG));
        canon.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));
        canon.setOrigin(15, 8);

        Turret turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET3_IMG));
        turret.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));

        return turret;
    }

    public Turret createLaserTurret() {
        TurretCanon canon = new LaserTurretCanon(200, 50, 100);
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET4_CANON_IMG));
        canon.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));
        canon.setOrigin(15, 8);

        Turret turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET4_IMG));
        turret.setDimension(new Dimension(TurretFactory.TURRET_SIZE, TurretFactory.TURRET_SIZE));

        return turret;
    }
}
