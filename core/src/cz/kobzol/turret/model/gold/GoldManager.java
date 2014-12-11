package cz.kobzol.turret.model.gold;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.graphics.IDrawable;

/**
 * Manages gold.
 */
public class GoldManager implements IDrawable {
    private int goldAmount;

    public GoldManager() {
        this(0);
    }

    public GoldManager(int goldAmount) {
        this.goldAmount = goldAmount;
    }

    public void deposit(IValuable valuable) {
        this.goldAmount += valuable.getGoldValue();
    }
    public void withdraw(IValuable valuable) {
        this.goldAmount -= valuable.getGoldValue();
    }
    public boolean hasEnoughGoldFor(IValuable valuable) {
        return valuable.getGoldValue() <= this.goldAmount;
    }

    @Override
    public void render(Batch batch, Camera camera) {

    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {

    }
}
