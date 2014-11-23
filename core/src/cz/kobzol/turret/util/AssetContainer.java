package cz.kobzol.turret.util;

import com.badlogic.gdx.assets.AssetManager;
import cz.kobzol.turret.model.GameObject;

/**
 * Contains template objects and assets.
 */
public class AssetContainer {
    private final AssetManager assetManager;
    private final ObjectManager objectManager;

    public AssetContainer(AssetManager assetManager, ObjectManager objectManager) {
        this.assetManager = assetManager;
        this.objectManager = objectManager;
    }

    public AssetManager getAssetManager() {
        return this.assetManager;
    }

    public ObjectManager getObjectManager() {
        return this.objectManager;
    }
}
