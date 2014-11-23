package cz.kobzol.turret.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Represents the screen that shows the game field.
 */
public class PlayScreen extends Screen {
    public PlayScreen(AssetContainer assetContainer) {
        super(assetContainer);
    }

    @Override
    public void start() {
        Slot slot = (Slot) this.getAssetContainer().getObjectManager().getObjectByKey("slot");
        Field field = new Field(this, new Vector2(100, 100), new Dimension(20, 20), slot);

        this.objects.add(field);
    }
}
