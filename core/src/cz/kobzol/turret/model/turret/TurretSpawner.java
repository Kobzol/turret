package cz.kobzol.turret.model.turret;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;
import cz.kobzol.turret.input.mouse.MouseState;

/**
 * Turret spawner.
 */
public class TurretSpawner extends SpriteObject implements IClickable {
    private final ClickContainer clickContainer;
    private final OnClickListener listener;
    private final Turret spawnedTurret;

    public TurretSpawner(Turret spawnedTurret, OnClickListener listener) {
        this.listener = listener;
        this.clickContainer = new ClickContainer(this);
        this.spawnedTurret = spawnedTurret;

        this.setDimension(spawnedTurret.getDimension());
    }

    public Turret getTurret() {
        return this.spawnedTurret;
    }

    @Override
    public ClickContainer getClickContainer() {
        return this.clickContainer;
    }

    @Override
    public void render(Batch batch, Camera camera) {
        spawnedTurret.render(batch, camera);
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onClick(MouseState mouseState) {
        this.listener.onClick(this);
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);

        spawnedTurret.setPosition(position);
    }

    public static interface OnClickListener {
        void onClick(TurretSpawner turretSpawner);
    }
}
