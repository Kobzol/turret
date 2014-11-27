package cz.kobzol.turret.services;

import com.badlogic.gdx.assets.AssetManager;
import cz.kobzol.turret.Game;

/**
 * Services locator for global access to objects.
 */
public class Locator {
    private AssetManager assetManager;
    private Game game;

    private Locator() {

    }

    public void provide(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    public void provide(Game game) {
        this.game = game;
    }

    public AssetManager getAssetManager() {
        assert(this.assetManager != null);

        return this.assetManager;
    }
    public Game getGame() {
        assert(this.game != null);

        return this.game;
    }
}
