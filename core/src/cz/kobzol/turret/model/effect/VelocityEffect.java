package cz.kobzol.turret.model.effect;

import cz.kobzol.turret.graphics.SpriteObject;

/**
 *
 */
public class VelocityEffect extends Effect {
    private float velocityCoefficient;
    private float old_speed;

    public VelocityEffect(long duration_ms, float velocityCoefficient) {
        super(duration_ms);

        this.velocityCoefficient = velocityCoefficient;
    }

    public void apply(SpriteObject object) {
        this.old_speed = object.getSpeed();
        object.setSpeed(this.old_speed * this.velocityCoefficient);
    }

    public void restore(SpriteObject object) {
        object.setSpeed(this.old_speed);
    }

    public float getVelocityCoefficient() {
        return this.velocityCoefficient;
    }

    @Override
    public boolean stackWith(Effect effect) {
        if (effect instanceof VelocityEffect) {
            VelocityEffect vel = (VelocityEffect) effect;

            if (vel.getVelocityCoefficient() < this.velocityCoefficient) {  // replaced itself with more slowing effect
                this.velocityCoefficient = vel.getVelocityCoefficient();
            }

            this.refresh();

            return true;    // let the effect perish
        }
        else return false;
    }
}
