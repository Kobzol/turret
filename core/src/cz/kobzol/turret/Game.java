package cz.kobzol.turret;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.PlayScreen;
import cz.kobzol.turret.model.Screen;
import cz.kobzol.turret.util.AssetContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game.
 */
public class Game implements IDrawable, IUpdatable {
    private final AssetContainer assetContainer;
    private final List<Screen> screens;
    private int activeScreenId;

    public Game(AssetContainer assetContainer) {
        this.screens = new ArrayList<Screen>();
        this.assetContainer = assetContainer;
    }

    public void start() {
        this.activeScreenId = 0;
        this.screens.add(new PlayScreen(this.assetContainer));

        this.screens.get(0).start();
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
