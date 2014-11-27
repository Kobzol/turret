package cz.kobzol.turret;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.GameScreen;
import cz.kobzol.turret.model.Screen;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;
import cz.kobzol.turret.util.ObjectManager;

/**
 * Represents the game.
 */
public final class Game {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;

    AssetContainer assetContainer;

    Screen screen;

    public Game() {
        this.assetContainer = this.createAssets();
        this.createGraphics();

        this.registerServices();
    }

    private AssetContainer createAssets() {
        AssetManager assetManager = new AssetManager();
        this.preloadAssets(assetManager);

        //ObjectManager objectManager = new ObjectLoader(assetManager).parseObjectManager(Gdx.files.internal("game_objects.xml"));

        return new AssetContainer(assetManager, new ObjectManager());
    }
    private void preloadAssets(AssetManager assetManager) {
        assetManager.finishLoading();
    }
    private void registerServices() {
        Locator.provide(this.assetContainer);
        Locator.provide(this);
    }
    private void createGraphics() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1600, 800);
    }

    private void handleInput() {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.camera.unproject(mousePosition);

        MouseState mouseState = new MouseState(mousePosition.x, mousePosition.y, Gdx.input.isButtonPressed(Input.Buttons.LEFT));

        this.screen.handleInput(mouseState);
    }

    public void update(float delta) {
        this.handleInput();
        this.screen.update(delta);
        this.render();
    }

    private void render() {
        this.renderSprite(this.batch, this.camera);
        this.renderShape(this.shapeRenderer, this.camera);
    }
    private void renderSprite(Batch batch, Camera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        screen.render(batch, camera);

        batch.end();
    }
    private void renderShape(ShapeRenderer shapeRenderer, Camera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        screen.renderShape(shapeRenderer, camera);

        shapeRenderer.end();
    }

    public void start() {
        this.screen = new GameScreen();
    }

    public void dispose() {
        this.assetContainer.getAssetManager().dispose();
    }
}
