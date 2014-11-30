package cz.kobzol.turret.util;

import com.badlogic.gdx.assets.AssetManager;

/**
 * Contains template objects and assets.
 */
public class AssetContainer {
    public static final String OBJECTS_XML = "obj/game_objects.xml";
    public static final String FONT_ARIAL = "font/arial.fnt";

    public static final String GRASS_IMG = "img/grass.png";
    public static final String PLATFORM_IMG = "img/platform.png";
    public static final String TURRET_BAR_IMG = "img/wood.jpg";

    public static final String TURRET1_IMG = "img/turrets/turret1_body.png";
    public static final String TURRET1_CANON_IMG = "img/turrets/turret1_canon.png";
    public static final String TURRET1_BULLET_IMG = "img/turrets/turret1_bullet.png";

    public static final String DEMON1_IMG = "img/demons/demon1.png";

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
