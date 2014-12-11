package cz.kobzol.turret.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Represents objects that can graphics themselves.
 */
public interface IDrawable {
    /**
     * Draw itself on a given batch.
     * @param batch batch
     * @param camera camera
     */
    void render(Batch batch, Camera camera);

    void renderShape(ShapeRenderer shapeRenderer, Camera camera);
}
