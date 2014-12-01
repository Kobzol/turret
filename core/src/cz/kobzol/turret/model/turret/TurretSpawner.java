package cz.kobzol.turret.model.turret;

import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;

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

        this.setTexture(spawnedTurret.getTexture());
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
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onClick() {
        this.listener.onClick(this);
    }

    public static interface OnClickListener {
        void onClick(TurretSpawner turretSpawner);
    }
}
