package cz.kobzol.turret;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.map.Level;
import cz.kobzol.turret.map.MapLoader;
import cz.kobzol.turret.model.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game.
 */
public class Game implements IUpdatable {
    private Level level;
    private AssetManager assetManager;

    public Game(AssetManager assetManager) {
        this.level = new Level();
        this.assetManager = assetManager;
    }

    public void prepare() {
        Level level = new MapLoader(assetManager).parseLevel(Gdx.files.internal("game_objects.xml"));

        for (GameObject obj : level.getObjects()) {
            this.level.addObject(obj);
        }
    }

    public List<GameObject> getObjects() {
        return this.level.getObjects();
    }

    @Override
    public void update() {
        for (GameObject obj : this.getObjects()) {
            if (obj instanceof IUpdatable) {
                ((IUpdatable) obj).update();
            }
        }
    }
}
