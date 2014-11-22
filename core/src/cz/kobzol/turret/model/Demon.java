package cz.kobzol.turret.model;

import cz.kobzol.turret.input.drag.DragContainer;
import cz.kobzol.turret.input.drag.IDraggable;

/**
 * Represents enemy demon.
 */
public class Demon extends SpriteObject implements IDraggable {
    private DragContainer container;

    public Demon() {
        this.container = new DragContainer(this);
    }

    @Override
    public DragContainer getDragContainer() {
        return this.container;
    }
}
