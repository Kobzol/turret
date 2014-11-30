package cz.kobzol.turret.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.input.click.Clicker;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game screen.
 */
public class GameScreen extends Screen {
    private BuildState buildState = new BuildState();
    private BitmapFont font;
    private Clicker clicker = new Clicker();

    private MouseState lastMouseState;

    private WaveSpawner waveSpawner = new WaveSpawner();

    private Field field = new Field();

    private List<Demon> demons = new ArrayList<Demon>();
    private List<Demon> flaggedDemons = new ArrayList<Demon>();

    private List<Turret> turrets = new ArrayList<Turret>();
    private TurretBar turretBar;
    private Button startWaveButton;

    private Turret selectedTurret;

    public GameScreen() {
        this.font = Locator.getAssetContainer().getAssetManager().get(AssetContainer.FONT_ARIAL, BitmapFont.class);
        this.font.setColor(Color.BLACK);

        this.prepareGUI();
        this.prepareWaves();
    }

    private void prepareGUI() {
        this.field.setPosition(new Vector2(0, 0));

        this.turretBar = new TurretBar(new TurretBar.TurretBarListener() {
            @Override
            public void onTurretSelected(Turret turret) {
                if (selectedTurret == null) {
                    selectedTurret = (Turret) turret.clone();
                }
            }
        });

        this.turretBar.setPosition(new Vector2(250, 650));
        this.turretBar.setDimension(new Dimension(500, 100));

        this.startWaveButton = new Button("Start wave", new Button.OnClickListener() {
            @Override
            public void onClick() {
                if (buildState.isBuilding()) {
                    startWave();
                }
            }
        });
        this.startWaveButton.setPosition(new Vector2(1000, 650));
    }
    private void prepareWaves() {
        Wave wave1 = new Wave();
        Demon demon = new Demon(500, 100.0f);
        demon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.DEMON1_IMG));
        demon.setDimension(new Dimension(30, 30));
        wave1.addSpawnee(demon, 5);

        this.waveSpawner.addWave(wave1);

        this.waveSpawner.addListener(new WaveSpawner.SpawnListener() {
            @Override
            public void onObjectSpawned(GameObject object) {
                spawnDemon((Demon) object);
            }

            @Override
            public void onWaveEnded() {

            }
        });
    }

    private void spawnDemon(Demon demon) {
        this.demons.add(demon);
        demon.setPosition(this.field.getSlotCoordinates(this.field.getStartSlot()));
    }
    private void startWave() {
        this.setDefenseState();
        this.waveSpawner.startSpawning();
    }
    private void stopWave() {
        this.setBuildState();
    }

    private void setBuildState() {
        this.buildState.setBuilding();
    }
    private void setDefenseState() {
        this.buildState.setDefending();
    }

    public MouseState getLastMouseState() {
        return this.lastMouseState;
    }
    public Turret getSelectedTurret() {
        return this.selectedTurret;
    }
    public List<Demon> getDemons() {
        return this.demons;
    }
    public Field getField() {
        return this.field;
    }

    public void onTurretSpawned(Turret turret) {
        this.selectedTurret = null;
        this.turrets.add(turret);
        this.field.registerObject(turret);
    }

    public void notifyDemonDied(Demon demon) {
        this.flagDemonDeath(demon);
    }
    public void notifyDemonFinished(Demon demon) {
        this.flagDemonDeath(demon);
    }

    private void flagDemonDeath(Demon demon) {
        this.flaggedDemons.add(demon);
    }
    private void removeFlaggedDemons() {
        boolean removed = this.flaggedDemons.size() > 0;

        this.demons.removeAll(this.flaggedDemons);

        if (removed && !this.buildState.isBuilding() && this.demons.size() == 0) {
            this.stopWave();
        }
    }

    @Override
    public void render(Batch batch, Camera camera) {
        Gdx.gl20.glClearColor(255, 255, 255, 255);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.turretBar.render(batch, camera);
        this.startWaveButton.render(batch, camera);
        this.field.render(batch, camera);

        for (Demon demon : this.demons) {
            demon.render(batch, camera);
        }

        for (Turret turret : this.turrets) {
            turret.render(batch, camera);
        }

        if (this.selectedTurret != null) {
            this.selectedTurret.render(batch, camera);
        }

        font.draw(batch, this.buildState.isBuilding() ? "building" : "defending", 0, camera.viewportHeight - 50);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {
        this.startWaveButton.renderShape(shapeRenderer, camera);

        for (Turret turret : this.turrets) {
            turret.renderShape(shapeRenderer, camera);
        }

        for (Demon demon : this.demons) {
            demon.renderShape(shapeRenderer, camera);
        }
    }

    @Override
    public void update(float delta) {
        this.removeFlaggedDemons();

        this.waveSpawner.update(delta);
        this.turretBar.update(delta);
        this.field.update(delta);

        for (Demon demon : this.demons) {
            demon.update(this, delta);
            this.field.registerObject(demon);
        }

        for (Turret turret : this.turrets) {
            turret.update(this, delta);
        }
    }

    @Override
    public void handleInput(MouseState mouseState) {
        this.lastMouseState = mouseState;

        this.clicker.handleClick(this.startWaveButton, mouseState);
        this.clicker.handleClick(this.field, mouseState);
        this.turretBar.handleInput(this.clicker, mouseState);

        if (this.selectedTurret != null) {
            this.selectedTurret.setPosition(mouseState.getMousePosition());
        }
    }
}
