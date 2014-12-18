package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.demon.Demon;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.List;

/**
 * Turret canon that shoots lasers.
 */
public class LaserTurretCanon extends TurretCanon {
    /**
     * Represents laser.
     */
    public static class Laser extends SpriteObject {

    }

    enum TurretState {
        FINDING_TARGET,
        LASER_TRAVELLING,
        SHOOTING
    }

    private static final float CANON_ROTATION = 300.0f;

    private TurretState state;
    private Demon target;
    private Laser laser;

    public LaserTurretCanon(float range, float damage, long fireDelay) {
        super(range, damage, fireDelay);

        this.state = TurretState.FINDING_TARGET;
        this.laser = new Laser();
        this.laser.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET4_LASER_IMG));
        this.laser.setColor(new Color(Color.RED));
        this.hideLaser(this.laser);
    }

    @Override
    public void render(Batch batch, Camera camera) {
        this.laser.render(batch, camera);

        super.render(batch, camera);
    }

    @Override
    public void handleDemons(List<Turret> turrets, List<Demon> demons, float delta) {
        super.handleDemons(turrets, demons, delta);

        if (this.target == null) {
            this.target = this.findTarget(demons);
        }
        else if (!this.isInRange(this.target) || !demons.contains(this.target)) {
            this.target = null;
            this.hideLaser(this.laser);
        }

        if (this.target == null) {
            LaserTurretCanon laserMate = null;

            for (Turret turret : turrets) {
                if (turret.getCanon() instanceof LaserTurretCanon &&
                    turret.getCanon() != this &&
                    ((LaserTurretCanon) turret.getCanon()).target != null) {
                        laserMate = (LaserTurretCanon) turret.getCanon();
                        break;
                }
            }

            if (laserMate != null) {
                Vector2 middleHalf = laserMate.target.getPosition().sub(laserMate.getPosition()).scl(0.5f);
                Vector2 middlePosition = laserMate.getPosition().add(middleHalf);

                this.moveLaserTo(middlePosition);
                this.dealDamage(laserMate.target);
            }
            else this.hideLaser(this.laser);
        }
        else if (this.target != null) {
            this.shootAt(this.target);
        }
    }

    private void hideLaser(Laser laser) {
        laser.setDimension(new Dimension(0, 0));
    }

    private void shootAt(Demon demon) {
        this.moveLaserTo(demon.getPosition());
        this.dealDamage(demon);

    }

    private void dealDamage(Demon demon) {
        if (this.fire_cooldown.resetIfReady()) {
            demon.receiveDamage(this.damage);
        }
    }

    private void moveLaserTo(Vector2 position) {
        Vector2 targetDir = position.sub(this.getPosition());

        this.laser.setDimension(new Dimension(10, (int) targetDir.len()));
        this.laser.setPosition(this.getPosition().add(targetDir.scl(0.5f)));
        this.laser.setDirection(targetDir.nor());
    }

    @Override
    public Object clone() {
        LaserTurretCanon canon = (LaserTurretCanon) super.clone();
        canon.laser = (Laser) this.laser.clone();

        return canon;
    }
}
