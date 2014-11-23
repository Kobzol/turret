package cz.kobzol.turret.model;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.input.drag.Dragger;
import cz.kobzol.turret.input.drag.IDraggable;
import cz.kobzol.turret.input.mouse.MouseState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single game screen.
 */
public abstract class Screen implements IDrawable, IUpdatable {
    protected final AssetManager assetManager;
    protected final List<GameObject> objects = new ArrayList<GameObject>();
    protected final Dragger dragger = new Dragger();

    public Screen(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public void draw(Batch batch) {
        for (GameObject object : this.objects) {
            if (object instanceof IDrawable) {
                ((IDrawable) object).draw(batch);
            }
        }
    }

    @Override
    public void drawShape(ShapeRenderer shapeRenderer) {
        for (GameObject object : this.objects) {
            if (object instanceof IDrawable) {
                ((IDrawable) object).drawShape(shapeRenderer);
            }
        }
    }

    @Override
    public void update() {
        for (GameObject object : this.objects) {
            if (object instanceof IUpdatable) {
                ((IUpdatable) object).update();
            }
        }
    }

    public List<GameObject> getObjects() {
        return this.objects;
    }

    public void handleDrag(MouseState mouseState) {
        for (GameObject object : this.objects) {
            if (object instanceof IDraggable) {
                this.dragger.handleDrag((IDraggable) object, mouseState);
            }
        }
    }
}
