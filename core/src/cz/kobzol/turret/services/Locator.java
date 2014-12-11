package cz.kobzol.turret.services;

import cz.kobzol.turret.Game;
import cz.kobzol.turret.util.AssetContainer;

/**
 * Services locator for global access to objects.
 */
public class Locator {
    private static AssetContainer assetContainer;
    private static Game game;

    private Locator() {

    }

    public static void provide(AssetContainer assetContainer) {
        Locator.assetContainer = assetContainer;
    }
    public static void provide(Game game) {
        Locator.game = game;
    }

    public static AssetContainer getAssetContainer() {
        assert(Locator.assetContainer != null);

        return Locator.assetContainer;
    }
    public static Game getGame() {
        assert(Locator.game != null);

        return Locator.game;
    }
}
