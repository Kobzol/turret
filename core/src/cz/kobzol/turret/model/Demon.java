package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.services.Locator;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Attacking demon.
 */
public class Demon extends SpriteObject {
    protected float max_health;
    protected float health;

    private FieldSlot target;

    private List<Effect> effects = new ArrayList<Effect>();

    public Demon(float max_health, float speed) {
        this.health = this.max_health = max_health;
        this.setSpeed(speed);
    }

    @Override
    public void render(Batch batch, Camera camera) {
        super.render(batch, camera);
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
            if (this.target == null) {
                this.target = this.findTarget(field);
            }

            Vector2 slotCoords = field.getSlotCoordinates(this.target);
            Vector2 direction = slotCoords.cpy().sub(this.getPosition()).nor();

            this.setMoveDirection(direction);
            this.move(delta);

            if (slotCoords.dst(this.getPosition()) <= delta * this.speed) {
                if (current == field.getEndSlot()) {
                    this.notifyFinished(gameScreen);
                }
                else this.target = null; // find new target
            }
        }

        this.restoreEffects();
    }

    public void addEffect(Effect newEffect) {
        boolean found = false;

        for (Effect effect : this.effects) {
            if (effect.getClass().equals(newEffect.getClass())) {
                effect.refresh();
                found = true;
                break;
            }
        }

        if (!found) {
            this.effects.add(newEffect);
        }
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

    private FieldSlot findTarget(Field field) {
        FieldSlot end = field.getEndSlot();
        FieldSlot current = field.getSlotForObject(this);

        Dimension fieldDimension = field.getFieldDimension();
        int slotCount = fieldDimension.width * fieldDimension.height;
        int directions[][] = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        boolean[] visited = new boolean[slotCount];
        int[] predecessors = new int[slotCount];
        int[] distances = new int[slotCount];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }

        distances[current.getIndex()] = 0;

        LinkedList<FieldSlot> queue = new LinkedList<FieldSlot>();
        queue.add(current);

        while (queue.size() > 0) {
            FieldSlot slot = queue.poll();

            visited[slot.getIndex()] = true;

            for (int[] shift : directions) {
                int newX = (int) slot.getPosition().x + shift[0];
                int newY = (int) slot.getPosition().y + shift[1];

                if (field.containsIndex(newX, newY)) {
                    Vector2 index = new Vector2(newX, newY);
                    FieldSlot incident = field.getSlotForIndex(index);

                    if (!visited[incident.getIndex()] && !incident.isPlatform()) {
                        visited[incident.getIndex()] = true;
                        predecessors[incident.getIndex()] = slot.getIndex();
                        distances[incident.getIndex()] = distances[slot.getIndex()] + 1;

                        if (incident == end) {
                            break;
                        }
                        else queue.add(incident);
                    }
                }
            }
        }

        assert(distances[end.getIndex()] < Integer.MAX_VALUE); // the path to the end was found

        int lastTarget = end.getIndex();
        int target = lastTarget;
        while (true) {
            if (field.getSlotForIndex(target) == current) {
                target = lastTarget;
                break;
            }
            else {
                lastTarget = target;
                target = predecessors[target];
            }
        }

        return field.getSlotForIndex(target);
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

        return demon;
    }
}
