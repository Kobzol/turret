package cz.kobzol.turret;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.drag.Dragger;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.map.Level;
import cz.kobzol.turret.map.MapLoader;
import cz.kobzol.turret.model.GameObject;
import cz.kobzol.turret.model.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game.
 */
public class Game implements IDrawable, IUpdatable {
    private final AssetManager assetManager;
    private final List<Screen> screens;
    private int activeScreenId;

    public Game(AssetManager assetManager) {
        this.screens = new ArrayList<Screen>();
        this.assetManager = assetManager;
    }

    public void start() {
        this.activeScreenId = 0;
    }

    public Screen getActiveScreen() {
        return this.screens.get(this.activeScreenId);
    }

    public void handleDrag(MouseState mouseState) {
        this.getActiveScreen().handleDrag(mouseState);
    }

    @Override
    public void update() {
        this.getActiveScreen().update();
    }

    @Override
    public void draw(Batch batch) {
        this.getActiveScreen().draw(batch);
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        this.getActiveScreen().drawShape(shapeRenderer);
    }
}
