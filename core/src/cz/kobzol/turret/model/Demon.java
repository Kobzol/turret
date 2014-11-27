package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;
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

    @Override
    public Object clone() {
        Demon obj = (Demon) super.clone();
        obj.container = new DragContainer(obj);

        return obj;
    }

    @Override
    public boolean isDraggable() {
        return true;
    }
}
