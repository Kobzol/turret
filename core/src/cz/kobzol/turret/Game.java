package cz.kobzol.turret;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.screen.GameScreen;
import cz.kobzol.turret.model.screen.Screen;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;
import cz.kobzol.turret.util.ObjectLoader;
import cz.kobzol.turret.util.ObjectManager;

import java.awt.Dimension;

/**
 * Represents the game.
 */
public final class Game {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    private AssetContainer assetContainer;

    private Dimension screenDimension = new Dimension(1530, 790);
    private Screen screen;

    public Game() {
        this.assetContainer = this.createAssets();
        this.createGraphics();

        this.registerServices();
    }

    private AssetContainer createAssets() {
        AssetManager assetManager = new AssetManager();
        this.preloadAssets(assetManager);

        ObjectManager objectManager = new ObjectLoader(assetManager).parseObjectManager(Gdx.files.internal(AssetContainer.OBJECTS_XML));

        return new AssetContainer(assetManager, objectManager);
    }
    private void preloadAssets(AssetManager assetManager) {
        assetManager.load(AssetContainer.FONT_ARIAL, BitmapFont.class);

        assetManager.load(AssetContainer.GRASS_IMG, Texture.class);
        assetManager.load(AssetContainer.PLATFORM_IMG, Texture.class);
        assetManager.load(AssetContainer.TARGET_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET_BAR_IMG, Texture.class);

        assetManager.load(AssetContainer.TURRET1_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET1_CANON_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET1_BULLET_IMG, Texture.class);

        assetManager.load(AssetContainer.TURRET2_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET2_CANON_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET2_BULLET_IMG, Texture.class);

        assetManager.load(AssetContainer.TURRET3_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET3_CANON_IMG, Texture.class);

        assetManager.load(AssetContainer.TURRET4_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET4_CANON_IMG, Texture.class);
        assetManager.load(AssetContainer.TURRET4_LASER_IMG, Texture.class);

        assetManager.load(AssetContainer.DEMON1_IMG, Texture.class);

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
        this.camera.setToOrtho(false, this.screenDimension.width, this.screenDimension.height);
    }

    public Screen getActiveScreen() {
        return this.screen;
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
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        screen.renderShape(shapeRenderer, camera);

        shapeRenderer.end();
    }

    public void start() {
        this.screen = new GameScreen(this.screenDimension);
    }

    public void dispose() {
        this.assetContainer.getAssetManager().dispose();
    }
}
