package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Turret canon that shoots lasers.
 */
public class LaserTurretCanon extends TurretCanon {
    private class Bullet extends SpriteObject {
        private final float damage;
        private Demon target;
        private final LaserTurretCanon canon;

        public Bullet(float damage, LaserTurretCanon canon) {
            this.canon = canon;
            this.damage = damage;
        }

        public void setTarget(Demon target) {
            this.target = target;
        }

        /**
         * Updates the bullet.
         * @param delta
         * @return True if the bullet hit the target and should be removed.
         */
        public boolean moveToTarget(float delta) {
            super.update(delta);

            if (this.target.collidesWith(this)) {
                this.target.receiveDamage(this.damage);
                return true;
            }

            this.setDirection(this.target.getPosition().sub(this.getPosition()).nor());
            this.move(delta);

            return false;
        }
    }

    enum TurretState {
        FINDING_TARGET,
        SHOOTING
    }

    private static final float CANON_ROTATION = 300.0f;

    private TurretState state;
    private Demon target;

    private Bullet templateBullet;
    private List<Bullet> bullets = new ArrayList<Bullet>();

    public LaserTurretCanon(float range, float damage, long fire_delay) {
        super(range, damage, fire_delay);

        this.templateBullet = new Bullet(this.damage, this);
        this.templateBullet.setSpeed(20.0f);
        this.templateBullet.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_BULLET_IMG));
        this.templateBullet.setDimension(new Dimension(8, 12));

        this.state = TurretState.FINDING_TARGET;
    }

    @Override
    public void render(Batch batch, Camera camera) {
        for (Bullet bullet : this.bullets) {
            bullet.render(batch, camera);
        }

        super.render(batch, camera);
    }

    private boolean isInRange(Demon demon) {
        return demon.getPosition().dst(this.getPosition()) <= this.range;
    }

    @Override
    public void handleDemons(List<Demon> demons, float delta) {
        super.handleDemons(demons, delta);

        for (Iterator<Bullet> it = this.bullets.iterator(); it.hasNext();) {
            Bullet bullet = it.next();

            if (bullet.moveToTarget(delta)) {
                it.remove();
            }
        }

        if (this.state == TurretState.FINDING_TARGET) {
            this.findTarget(demons);
            this.rotateCanon(delta);
        }
        else if (this.state == TurretState.SHOOTING) {
            if (demons.contains(this.target) && this.isInRange(this.target)) {
                this.shootAt(this.target);
            }
            else this.state = TurretState.FINDING_TARGET;
        }
    }

    private void shootAt(Demon demon) {
        this.setDirection(demon.getPosition().sub(this.getPosition()));

        if (this.fire_cooldown.resetIfReady()) {
            Bullet bullet = (Bullet) this.templateBullet.clone();
            bullet.setTarget(demon);
            bullet.setPosition(this.getPosition());
            bullet.setDirection(this.getDirection());

            this.bullets.add(bullet);
        }
    }
    private void findTarget(List<Demon> demons) {
        this.target = null;

        for (Demon demon : demons) {
            if (this.isInRange(demon)) {
                this.target = demon;
                break;
            }
        }
    }
    private void rotateCanon(float delta) {
        float canon_rotation = LaserTurretCanon.CANON_ROTATION * delta;

        if (this.target != null) {
            Vector2 targetPosition = this.target.getPosition();
            float angle = targetPosition.sub(this.getPosition()).angle();
            double difference = Math.abs(angle - this.getRotation());

            if (difference < canon_rotation) {
                this.state = TurretState.SHOOTING;
            }
            else {
                this.rotate(canon_rotation);
                double new_difference = Math.abs(angle - this.getRotation());

                if (new_difference > difference) {
                    this.rotate(-2 * canon_rotation);
                }
            }
        }
        else {
            float angle = this.getRotation();
            double difference = Math.abs(90.0f - angle);

            if (difference > canon_rotation) {
                if (angle > 90 && angle <= 270) {
                    this.rotate(-canon_rotation);
                }
                else this.rotate(canon_rotation);
            }
            else this.setRotation(90);
        }
    }
}
