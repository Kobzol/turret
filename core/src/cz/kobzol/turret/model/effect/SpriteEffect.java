package cz.kobzol.turret.model.effect;

import com.badlogic.gdx.graphics.Color;
import cz.kobzol.turret.graphics.SpriteObject;

/**
 * Effect that changes object's sprite.
 */
public class SpriteEffect extends Effect {
    private Color oldColor;
    private final Color newColor;

    public SpriteEffect(long duration_ms, Color color) {
        super(duration_ms);

        this.newColor = color;
    }

    @Override
    public void apply(SpriteObject object) {
        this.oldColor = object.getColor();
        object.setColor(this.newColor);
    }

    @Override
    public void restore(SpriteObject object) {
        object.setColor(this.oldColor);
    }

    @Override
    public boolean stackWith(Effect effect) {
        if (effect instanceof SpriteEffect) {
            SpriteEffect spr = (SpriteEffect) effect;

            this.refresh();

            return true;
        }
        else return false;
    }
}
