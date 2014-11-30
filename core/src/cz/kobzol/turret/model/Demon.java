package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.services.Locator;

import java.awt.Dimension;

/**
 * Attacking demon.
 */
public class Demon extends SpriteObject {
    protected float max_health;
    protected float health;

    public Demon(float max_health) {
        this.health = this.max_health = max_health;
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {
        super.renderShape(shapeRenderer, camera);

        Vector2 position = this.getPosition();
        Dimension dimension = this.getDimension();

        float healthBarWidth = this.dimension.width;
        float currentHealthWidth = healthBarWidth * (this.health / this.max_health);
        float healthBarHeight = 6.0f;

        if (this.health <= 0) {
            currentHealthWidth = 0;
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x - dimension.width / 2, position.y + dimension.height / 2 + 10, healthBarWidth, healthBarHeight);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x - dimension.width / 2, position.y + dimension.height / 2 + 10, currentHealthWidth, healthBarHeight);
    }

    public void update(GameScreen gameScreen, float delta) {
        this.setPosition(new Vector2(this.getPosition().x + 5, this.getPosition().y));

        Field field = gameScreen.getField();

        if (field.isAtFinish(this)) {
            gameScreen.notifyDemonFinished(this);
        }
    }

    public void receiveDamage(float damage) {
        this.health -= damage;

        if (this.health <= 0.0f) {
            this.notifyDeath();
        }
    }

    private void notifyDeath() {
        ((GameScreen) Locator.getGame().getActiveScreen()).notifyDemonDied(this);
    }
}
