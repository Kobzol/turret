package cz.kobzol.turret.services;

import cz.kobzol.turret.Game;
import cz.kobzol.turret.util.AssetContainer;

/**
 * Services locator for global access to objects.
 */
public class Locator {
    private AssetContainer assetContainer;
    private Game game;

    private Locator() {

    }

    public void provide(AssetContainer assetManager) {
        this.assetContainer = assetManager;
    }
    public void provide(Game game) {
        this.game = game;
    }

    public AssetContainer getAssetContainer() {
        assert(this.assetContainer != null);

        return this.assetContainer;
    }
    public Game getGame() {
        assert(this.game != null);

        return this.game;
    }
}
