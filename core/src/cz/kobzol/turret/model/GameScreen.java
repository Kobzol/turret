package cz.kobzol.turret.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

/**
 * Represents the game screen.
 */
public class GameScreen extends Screen {
    private BuildState buildState = new BuildState();

    private WaveSpawner waveSpawner = new WaveSpawner();

    private Field field = new Field(new Vector2(0, 0));

    public GameScreen() {
        Demon demon = new Demon();
        demon.setTexture(Locator.getAssetContainer().getAssetManager().get(AssetContainer.DEMON_IMG, Texture.class));

        Wave wave = new Wave();
        wave.addSpawnee(demon, 10);

        this.waveSpawner.addWave(wave);
        this.waveSpawner.addListener(new WaveSpawner.SpawnListener() {
            @Override
            public void onObjectSpawned(GameObject object) {

            }

            @Override
            public void onWaveEnded() {
                setBuildState();
            }
        });

        this.waveSpawner.startSpawning();
    }

    private void setBuildState() {
        this.buildState.setBuilding();
    }
    private void setDefenseState() {
        this.buildState.setDefending();
    }

    @Override
    public void render(Batch batch, Camera camera) {
        Gdx.gl20.glClearColor(255, 255, 255, 255);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.field.render(batch, camera);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {

    }

    @Override
    public void update(float delta) {
        this.waveSpawner.update(delta);

        this.field.update(delta);
    }

    @Override
    public void handleInput(MouseState mouseState) {

    }
}
