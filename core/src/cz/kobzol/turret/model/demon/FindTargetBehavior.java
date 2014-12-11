package cz.kobzol.turret.model.demon;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.model.field.Field;
import cz.kobzol.turret.model.field.FieldSlot;
import cz.kobzol.turret.model.field.PathFinder;
import cz.kobzol.turret.model.screen.GameScreen;

import java.util.List;

/**
 * Demon behavior that finds target in a filed.
 */
public class FindTargetBehavior extends DemonBehavior {
    private PathFinder pathFinder = new PathFinder();

    public FindTargetBehavior() {
        super();
    }

    public FindTargetBehavior(DemonBehavior demonBehavior) {
        super(demonBehavior);
    }

    @Override
    public void render(Demon demon, Batch batch, Camera camera) {
        super.render(demon, batch, camera);
    }

    @Override
    public void renderShape(Demon demon, ShapeRenderer shapeRenderer, Camera camera) {
        super.renderShape(demon, shapeRenderer, camera);
    }

    @Override
    public void update(GameScreen gameScreen, Demon demon, float delta) {
        super.update(gameScreen, demon, delta);

        Field field = gameScreen.getField();
        FieldSlot current = field.getSlotForObject(demon);

        if (current != null) { // wait for field to register this object
            if (!this.pathFinder.isPathValid(field)) {
                List<FieldSlot> path = this.pathFinder.findPath(field);

                assert(path != null);

                this.pathFinder.setPath(path);
            }

            Vector2 slotCoords = field.getSlotCoordinates(this.pathFinder.getNextTarget());
            Vector2 direction = slotCoords.cpy().sub(demon.getPosition()).nor();

            demon.setMoveDirection(direction);
            demon.move(delta);

            if (slotCoords.dst(demon.getPosition()) <= delta * demon.getSpeed()) {
                if (current == field.getEndSlot()) {
                    demon.notifyFinished(gameScreen);
                }
                else this.pathFinder.advanceInPath();
            }
        }
    }

    @Override
    public Object clone() {
        FindTargetBehavior behavior = (FindTargetBehavior) super.clone();
        behavior.pathFinder = new PathFinder();

        return behavior;
    }
}
