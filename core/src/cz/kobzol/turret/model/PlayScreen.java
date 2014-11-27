package cz.kobzol.turret.model;

import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Represents the screen that shows the game field.
 */
public class PlayScreen extends Screen {
    private Field field;

    public PlayScreen(AssetContainer assetContainer) {
        super(assetContainer);
    }

    @Override
    public void start() {
        Slot slot = (Slot) this.getAssetContainer().getObjectManager().getObjectByKey("slot");
        this.field = new Field(this, new Vector2(100, 100), new Dimension(40, 20), slot);

        this.objects.add(field);
        this.objects.add(this.getAssetContainer().getObjectManager().getObjectByKey("demon"));
    }

    @Override
    public boolean receiveObject(IDraggable draggableObject) {
        return this.field.receiveObject(draggableObject);
    }
}
