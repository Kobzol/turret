package cz.kobzol.turret.model.demon;

import com.badlogic.gdx.graphics.Texture;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Creates demons.
 */
public class DemonFactory {
    public Demon createBasicDemon() {
        Demon demon = new Demon(250, 100.0f, new FindTargetBehavior());
        demon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.DEMON1_IMG));
        demon.setDimension(new Dimension(30, 30));
        demon.setGoldValue(50);

        return demon;
    }

    public Demon createTankDemon() {
        Demon demon = new Demon(1000, 80.0f, new FindTargetBehavior());
        demon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.DEMON1_IMG));
        demon.setDimension(new Dimension(30, 30));
        demon.setGoldValue(100);

        return demon;
    }

    public Demon createQuickDemon() {
        Demon demon = new Demon(200, 200.0f, new FindTargetBehavior());
        demon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.DEMON1_IMG));
        demon.setDimension(new Dimension(30, 30));
        demon.setGoldValue(60);

        return demon;
    }
}
