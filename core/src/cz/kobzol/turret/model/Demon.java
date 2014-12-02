package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.effect.Effect;
import cz.kobzol.turret.model.field.Field;
import cz.kobzol.turret.model.field.FieldSlot;
import cz.kobzol.turret.model.field.PathFinder;
import cz.kobzol.turret.model.screen.GameScreen;
import cz.kobzol.turret.services.Locator;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Attacking demon.
 */
public class Demon extends SpriteObject {
    protected float max_health;
    protected float health;

    private PathFinder pathFinder;

    private List<Effect> effects = new ArrayList<Effect>();

    public Demon(float max_health, float speed) {
        this.health = this.max_health = max_health;
        this.setSpeed(speed);

        this.pathFinder = new PathFinder();
    }

    @Override
    public void render(Batch batch, Camera camera) {
        this.applyEffects();

        super.render(batch, camera);

        this.restoreEffects();
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
        super.update(delta);

        this.updateEffects(delta);

        this.applyEffects();

        Field field = gameScreen.getField();

        FieldSlot current = field.getSlotForObject(this);

        if (current != null) { // wait for field to register this object
            if (!this.pathFinder.isPathValid()) {
                List<FieldSlot> path = this.pathFinder.findPath(field);

                assert(path != null);

                this.pathFinder.setPath(path);
            }

            Vector2 slotCoords = field.getSlotCoordinates(this.pathFinder.getNextTarget());
            Vector2 direction = slotCoords.cpy().sub(this.getPosition()).nor();

            this.setMoveDirection(direction);
            this.move(delta);

            if (slotCoords.dst(this.getPosition()) <= delta * this.speed) {
                if (current == field.getEndSlot()) {
                    this.notifyFinished(gameScreen);
                }
                else this.pathFinder.advanceInPath();
            }
        }

        this.restoreEffects();
    }

    public void addEffect(Effect newEffect) {
        for (Effect effect : this.effects) {
            if (effect.stackWith(newEffect)) {
                return;
            }
        }

        this.effects.add(newEffect); // the effect was not stacked
    }

    private void applyEffects() {
        for (Effect effect : this.effects) {
            effect.apply(this);
        }
    }
    private void restoreEffects() {
        for (Effect effect : this.effects) {
            effect.restore(this);
        }
    }

    private void updateEffects(float delta) {
        for (Iterator<Effect> it = this.effects.iterator(); it.hasNext();) {
            Effect effect = it.next();

            if (effect.update(delta)) {
                it.remove();
            }
        }
    }

    private void notifyFinished(GameScreen gameScreen) {
        gameScreen.notifyDemonFinished(this);
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

    @Override
    public Object clone() {
        Demon demon = (Demon) super.clone();
        demon.effects = new ArrayList<Effect>();
        demon.effects.addAll(this.effects);
        demon.pathFinder = new PathFinder();

        return demon;
    }
}
