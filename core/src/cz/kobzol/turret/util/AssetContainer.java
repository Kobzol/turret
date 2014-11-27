package cz.kobzol.turret.util;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Contains template objects and assets.
 */
public class AssetContainer {
    public static final String OBJECTS_XML = "obj/game_objects.xml";
    public static final String FONT_ARIAL = "font/arial.fnt";

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
