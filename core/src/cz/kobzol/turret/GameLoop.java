package cz.kobzol.turret;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.input.drag.Dragger;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.GameObject;

import java.util.List;

public class GameLoop extends ApplicationAdapter {
	SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    OrthographicCamera camera;
    AssetManager assetManager;

    Dragger dragger = new Dragger();

    Game game;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.shapeRenderer = new ShapeRenderer();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 2000, 1000);
        this.assetManager = this.preloadAssets();

        this.game = new Game(this.assetManager);
        this.game.start();
	}

    private AssetManager preloadAssets() {
        AssetManager assetManager = new AssetManager();

        assetManager.load("slot.png", Texture.class);

        assetManager.finishLoading();
        return assetManager;
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();

        this.renderSprite();
        this.renderShape();

        this.handleInput();

        this.game.update();
	}

    private void renderSprite() {
        this.batch.setProjectionMatrix(this.camera.combined);
        this.batch.begin();

        this.game.draw(this.batch);

        this.batch.end();
    }
    private void renderShape() {
        this.shapeRenderer.setProjectionMatrix(this.camera.combined);
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        this.game.drawShape(this.shapeRenderer);

        this.shapeRenderer.end();
    }

    private void handleInput() {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.camera.unproject(mousePosition);

        MouseState mouseState = new MouseState(mousePosition.x, mousePosition.y, Gdx.input.isButtonPressed(Input.Buttons.LEFT));

        this.game.handleDrag(mouseState);
    }
}
