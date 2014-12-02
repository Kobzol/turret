package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.Clicker;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.Demon;
import cz.kobzol.turret.model.effect.DamageOverTimeEffect;
import cz.kobzol.turret.model.effect.SpriteEffect;
import cz.kobzol.turret.model.effect.VelocityEffect;
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

    private List<TurretSpawner> spawners = new ArrayList<TurretSpawner>();

    public TurretBar(TurretBarListener listener) {
        this.listener = listener;

        this.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET_BAR_IMG));

        this.prepareTurretSpawners();

        this.repositionSpawners();
    }

    private void prepareTurretSpawners() {
        TurretCanon canon = new MissileTurretCanon(200, 50, 100, 300, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_CANON_IMG));
        canon.setDimension(new Dimension(30, 30));
        canon.setOrigin(15, 8);

        Turret turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET1_IMG));
        turret.setDimension(new Dimension(30, 30));

        TurretSpawner turretSpawner = new TurretSpawner(turret, new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });

        this.spawners.add(turretSpawner);

        canon = new MissileTurretCanon(200, 50, 500, 200, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
                demon.addEffect(new VelocityEffect(1000, 0.5f));
                demon.addEffect(new SpriteEffect(1000, new Color(0, 0, 1, 0.5f)));
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_CANON_IMG));
        canon.setDimension(new Dimension(30, 30));
        canon.setOrigin(15, 8);

        turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_IMG));
        turret.setDimension(new Dimension(30, 30));

        turretSpawner = new TurretSpawner(turret, new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });

        this.spawners.add(turretSpawner);

        canon = new MissileTurretCanon(200, 50, 500, 200, new MissileTurretCanon.IDamager() {
            @Override
            public void dealDamage(Demon demon, MissileTurretCanon.Bullet bullet) {
                demon.receiveDamage(bullet.getDamage());
                demon.addEffect(new DamageOverTimeEffect(1000, 3, 30));
                demon.addEffect(new SpriteEffect(1000, new Color(0, 1, 0, 0.8f)));
            }
        });
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_CANON_IMG));
        canon.setDimension(new Dimension(30, 30));
        canon.setOrigin(15, 8);

        turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_IMG));
        turret.setDimension(new Dimension(30, 30));

        turretSpawner = new TurretSpawner(turret, new TurretSpawner.OnClickListener() {
            @Override
            public void onClick(TurretSpawner turretSpawner) {
                TurretBar.this.listener.onTurretSelected(turretSpawner.getTurret());
            }
        });

        this.spawners.add(turretSpawner);

        canon = new LaserTurretCanon(200, 50, 100);
        canon.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_CANON_IMG));
        canon.setDimension(new Dimension(30, 30));
        canon.setOrigin(15, 8);

        turret = new Turret(canon);
        turret.setTexture((Texture) Locator.getAssetContainer().getAssetManager().get(AssetContainer.TURRET2_IMG));
        turret.setDimension(new Dimension(30, 30));

        turretSpawner = new TurretSpawner(turret, new TurretSpawner.OnClickListener() {
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
