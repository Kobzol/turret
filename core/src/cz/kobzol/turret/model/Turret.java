package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.services.Locator;

import java.util.List;

/**
 * Represents a turret.
 */
public class Turret extends SpriteObject {
    protected TurretCanon canon;

    public Turret(TurretCanon canon) {
        this.canon = canon;

        this.updateCanonPosition();
    }

    private void updateCanonPosition() {
        this.canon.setPosition(new Vector2(this.getPosition().x, this.getPosition().y + this.getDimension().height / 4));
    }

    public void update(GameScreen gameScreen, float delta) {
        super.update(delta);

        List<Demon> demons = gameScreen.getDemons();
        this.canon.handleDemons(demons, delta);
    }

    @Override
    public void render(Batch batch, Camera camera) {
        super.render(batch, camera);
        this.canon.render(batch, camera);
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {
        super.renderShape(shapeRenderer, camera);

        GameScreen scr = (GameScreen) Locator.getGame().getActiveScreen();

        if (this.getBoundingBox().contains(scr.getLastMouseState().getMousePosition())) {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(this.getPosition().x, this.getPosition().y, this.canon.range);
        }
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        this.updateCanonPosition();
    }

    @Override
    public Object clone() {
        Turret turret = (Turret) super.clone();
        turret.canon = (TurretCanon) this.canon.clone();

        return turret;
    }
}
