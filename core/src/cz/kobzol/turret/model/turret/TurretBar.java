package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.Clicker;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Turret bar to create turrets.
 */
public class TurretBar extends SpriteObject {
    private final TurretBarListener listener;

    private List<TurretSpawner> spawners = new ArrayList<TurretSpawner>();

    public TurretBar(TurretBarListener listener) {
        this.listener = listener;

        this.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET_BAR_IMG));

        this.prepareTurretSpawners();

        this.repositionSpawners();
    }

    private void prepareTurretSpawners() {
        TurretFactory factory = new TurretFactory();

        TurretSpawner turretSpawner = new TurretSpawner(factory.createBulletTurret(), new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });
        this.spawners.add(turretSpawner);

        turretSpawner = new TurretSpawner(factory.createLaserTurret(), new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });
        this.spawners.add(turretSpawner);

        turretSpawner = new TurretSpawner(factory.createPoisonTurret(), new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });
        this.spawners.add(turretSpawner);

        turretSpawner = new TurretSpawner(factory.createLaserTurret(), new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });
        this.spawners.add(turretSpawner);
    }

    private void repositionSpawners() {
        Vector2 leftMargin = new Vector2(this.getPosition().x - this.getDimension().width / 2 + 50, this.getPosition().y);

        for (int i = 0; i < this.spawners.size(); i++) {
            this.spawners.get(i).setPosition(new Vector2(leftMargin.x + i * this.spawners.get(i).getDimension().width, leftMargin.y));
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

    public void handleInput(Clicker clicker, MouseState mouseState) {
        for (TurretSpawner spawner : this.spawners) {
            clicker.handleClick(spawner, mouseState);
        }
    }

    public static interface TurretBarListener {
        void onTurretSelected(Turret turret);
    }
}
