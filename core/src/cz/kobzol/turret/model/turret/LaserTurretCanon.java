package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.Demon;
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
    public static interface IDamager {
        public void dealDamage(Demon demon, LaserTurretCanon.Bullet bullet);
    }
    protected class Bullet extends SpriteObject {
        private final float damage;
        private Demon target;
        private final LaserTurretCanon canon;
        private final IDamager iDamager;

        public Bullet(float damage, LaserTurretCanon canon, float speed, IDamager iDamager) {
            this.canon = canon;
            this.damage = damage;

            this.setSpeed(speed);
            this.iDamager = iDamager;
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
                this.iDamager.dealDamage(this.target, this);
                return true;
            }

            this.setMoveDirection(this.target.getPosition().sub(this.getPosition()).nor());
            this.setDirection(this.getMoveDirection());
            this.move(delta);

            return false;
        }

        protected float getDamage() {
            return this.damage;
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

    public LaserTurretCanon(float range, float damage, long fireDelay, float bulletSpeed, IDamager iDamager) {
        super(range, damage, fireDelay);

        this.templateBullet = this.createTemplateBullet(bulletSpeed, iDamager);

        this.state = TurretState.FINDING_TARGET;
    }

    protected Bullet createTemplateBullet(float bulletSpeed, IDamager iDamager) {
        Bullet bullet = new Bullet(this.damage, this, bulletSpeed, iDamager);
        bullet.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_BULLET_IMG));
        bullet.setDimension(new Dimension(8, 12));

        return bullet;
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

            Vector2 position = this.getPosition();
            bullet.setPosition(new Vector2(position.x, position.y - 10));
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
