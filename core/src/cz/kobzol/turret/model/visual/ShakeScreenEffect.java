package cz.kobzol.turret.model.visual;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Represents visual effect.
 */
public class ShakeScreenEffect extends VisualEffect {
    private int counter = 2;
    private int direction = 1;

    public ShakeScreenEffect() {
        super(250);
    }

    @Override
    public void render(Batch batch, Camera camera) {
        this.counter += this.direction;

        camera.translate(this.direction, 0, 0);
        camera.update();

        if (this.counter == 4 || this.counter == 0) {
            this.direction *= -1;
        }
    }
}
