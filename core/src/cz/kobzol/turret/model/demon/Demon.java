package cz.kobzol.turret.model.demon;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.model.effect.Effect;
import cz.kobzol.turret.model.gold.IValuable;
import cz.kobzol.turret.model.screen.GameScreen;
import cz.kobzol.turret.util.IObservable;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Attacking demon.
 */
public class Demon extends SpriteObject implements IObservable, IValuable {
    private List<ObservableListener> listeners = new ArrayList<ObservableListener>();

    protected float max_health;
    protected float health;

    private int goldValue;

    private DemonBehavior demonBehavior;

    private List<Effect> effects = new ArrayList<Effect>();

    public Demon(float max_health, float speed, DemonBehavior demonBehavior) {
        this.health = this.max_health = max_health;
        this.setSpeed(speed);

        this.demonBehavior = demonBehavior;
    }

    @Override
    public int getGoldValue() {
        return this.goldValue;
    }

    @Override
    public void setGoldValue(int value) {
        this.goldValue = value;
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

        if (this.health == this.max_health) {   // draw health only if the demon is damaged
            return;
        }

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

        if (this.health <= 0.0f) {
            this.notifyDeath(gameScreen);
        }

        this.updateEffects(delta);

        this.applyEffects();

        this.demonBehavior.update(gameScreen, this, delta);

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

    public void notifyFinished(GameScreen gameScreen) {
        gameScreen.notifyDemonFinished(this);
        this.notifyRemove();
    }
    public void notifyDeath(GameScreen gameScreen) {
        gameScreen.notifyDemonDied(this);
        this.notifyRemove();
    }

    public void receiveDamage(float damage) {
        this.health -= damage;
    }

    @Override
    public Object clone() {
        Demon demon = (Demon) super.clone();
        demon.effects = new ArrayList<Effect>();
        demon.effects.addAll(this.effects);
        demon.demonBehavior = (DemonBehavior) this.demonBehavior.clone();

        return demon;
    }

    private void notifyRemove() {
        for (ObservableListener listener : this.listeners) {
            listener.onRemove(this);
        }
    }

    @Override
    public void addListener(ObservableListener listener) {
        this.listeners.add(listener);
    }
}
