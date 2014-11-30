package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.HashMap;

/**
 * Field with demons and turrets.
 */
public class Field extends SpriteObject implements IClickable {
    private final ClickContainer clickContainer;

    private final Dimension dimension = new Dimension(52, 20);
    private final Dimension slotDimension = new Dimension(30, 30);

    private Texture slotTexture;
    private Texture platformTexture;

    private FieldSlot[][] slots;

    private HashMap<SpriteObject, Vector2> objectPositions = new HashMap<SpriteObject, Vector2>();

    public Field() {
        this.clickContainer = new ClickContainer(this);

        this.slotTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.GRASS_IMG);
        this.platformTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.PLATFORM_IMG);

        this.setDimension(new Dimension(this.dimension.width * this.slotDimension.width, this.dimension.height * this.slotDimension.height));

        this.slots = this.preparePlatforms(this.dimension.width, this.dimension.height);
    }

    private FieldSlot[][] preparePlatforms(int width, int height) {
        FieldSlot[][] slots = new FieldSlot[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[y][x] = new FieldSlot(x, y);
                slots[y][x].setPlatform(true);
            }
        }

        for (int i = 0; i < width; i++) {
            slots[height / 2][i].setPlatform(false);
        }

        return slots;
    }
    public boolean registerObject(SpriteObject object) {
        if (this.isInside(object.getPosition())) {
            Vector2 position = this.getSlotPosition(object.getPosition());

            if (this.objectPositions.containsKey(object)) {
                Vector2 oldPosition = this.objectPositions.get(object);
                this.getSlot(oldPosition).removeObject(object);
            }

            objectPositions.put(object, position);
            this.getSlot(position).addObject(object);

            return true;
        }
        else if (this.objectPositions.containsKey(object)) {
            Vector2 oldPosition = this.objectPositions.get(object);
            this.getSlot(oldPosition).removeObject(object);
        }

        return false;
    }

    public boolean isInside(Vector2 coords) {
        int marginX = (int) (this.dimension.getWidth() * this.slotDimension.getWidth());
        int marginY = (int) (this.dimension.getHeight() * this.slotDimension.getHeight());

        Vector2 anchorPoint = this.getPosition();

        Rectangle rectangle = new Rectangle(anchorPoint.x, anchorPoint.y,marginX,marginY);

        return rectangle.contains(coords) && coords.x < marginX && coords.y < marginY;
    }

    public Vector2 getSlotPosition(Vector2 coords) {
        coords.sub(this.getPosition()); // normalized position

        int x = (int) coords.x / (int) this.slotDimension.getWidth();
        int y = (int) coords.y / (int) this.slotDimension.getHeight();

        return new Vector2(x, y);
    }
    public Vector2 getSlotCoordinates(Vector2 slotPosition) {
        int x = (int) (slotPosition.x * this.slotDimension.width);
        int y = (int) (slotPosition.y * this.slotDimension.height);

        Vector2 coords = new Vector2(x, y);
        coords.add(this.getPosition());
        coords.add(this.slotDimension.width / 2, this.slotDimension.height / 2);

        return coords;
    }

    public FieldSlot getSlot(Vector2 slotPosition) {
        return this.slots[(int) slotPosition.y][(int) slotPosition.x];
    }

    public Vector2 getStartPosition() {
        return this.getSlotCoordinates(new Vector2(0, this.dimension.height / 2));
    }
    public Vector2 getEndPosition() {
        return this.getSlotCoordinates(new Vector2(this.dimension.width - 1, this.dimension.height / 2));
    }
    public boolean isAtFinish(SpriteObject object) {
        Vector2 finishPosition = this.getSlotPosition(this.getEndPosition());
        FieldSlot finishSlot = this.getSlot(finishPosition);

        return finishSlot.containsObject(object);
    }

    @Override
    public void render(Batch batch, Camera camera) {
        Vector2 anchorPoint = this.getPosition();

        for (int y = 0; y < dimension.getHeight(); y++) {
            for (int x = 0; x < dimension.getWidth(); x++) {
                double xPos = anchorPoint.x + x * slotDimension.getWidth();
                double yPos = anchorPoint.y + y * slotDimension.getHeight();

                batch.draw(this.slotTexture, (float) xPos, (float) yPos);

                if (this.slots[y][x].isPlatform()) {
                    batch.draw(this.platformTexture, (float) xPos, (float) yPos);
                }
            }
        }
    }

    @Override
    public void renderShape(ShapeRenderer shapeRenderer, Camera camera) {

    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(this.getPosition().x, this.getPosition().y, this.getDimension().width, this.getDimension().height);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public ClickContainer getClickContainer() {
        return this.clickContainer;
    }

    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public void onClick() {
        GameScreen scr = (GameScreen) Locator.getGame().getActiveScreen();

        if (scr.getSelectedTurret() != null) {
            Turret turret = scr.getSelectedTurret();

            Vector2 position = this.getSlotPosition(turret.getPosition());

            if (this.getSlot(position).isPlatform() && this.getSlot(position).isEmpty()) {
                if (this.registerObject(turret)) {
                    scr.onTurretSpawned(turret);

                    Vector2 coords = this.getSlotCoordinates(position);

                    turret.setPosition(coords);
                }
            }
        }
    }
}
