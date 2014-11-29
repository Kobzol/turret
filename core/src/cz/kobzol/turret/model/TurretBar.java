package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.Clicker;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * Turret bar to create turrets.
 */
public class TurretBar extends SpriteObject {
    private final TurretBarListener listener;
    private final Dimension dimension = new Dimension(500, 100);

    private List<TurretSpawner> spawners = new ArrayList<TurretSpawner>();

    private TurretSpawner selectedSpawner;

    public TurretBar(TurretBarListener listener) {
        this.listener = listener;

        this.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET_BAR_IMG));

        Turret turret = (Turret) Locator.getAssetContainer().getObjectManager().getObjectByKey("turret");
        TurretSpawner turretSpawner = new TurretSpawner(turret, new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });

        this.repositionSpawners();

        this.spawners.add(turretSpawner);
    }

    private void repositionSpawners() {
        for (int i = 0; i < this.spawners.size(); i++) {
            this.spawners.get(i).setPosition(new Vector2(this.getPosition().x + i * this.spawners.get(i).getDimension().width, this.getPosition().y));
        }
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);

        this.repositionSpawners();
    }

    @Override
    public void render(Batch batch, Camera camera) {
        super.render(batch, camera);

        for (TurretSpawner spawner : this.spawners) {
            spawner.render(batch, camera);
        }
    }

    public static interface TurretBarListener {
        void onTurretSelected(Turret turret);
    }

    public void handleInput(Clicker clicker, MouseState mouseState) {
        for (TurretSpawner spawner : this.spawners) {
            clicker.handleClick(spawner, mouseState);
        }
    }
}
