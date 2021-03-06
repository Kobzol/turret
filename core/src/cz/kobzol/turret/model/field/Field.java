package cz.kobzol.turret.model.field;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;
import cz.kobzol.turret.input.click.ClickContainer;
import cz.kobzol.turret.input.click.IClickable;
import cz.kobzol.turret.input.mouse.MouseState;
import cz.kobzol.turret.model.screen.GameScreen;
import cz.kobzol.turret.model.turret.Turret;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;
import java.util.HashMap;

/**
 * Field with demons and turrets.
 */
public class Field extends SpriteObject implements IClickable {
    private final ClickContainer clickContainer;

    private final Dimension fieldDimension;
    private final Dimension slotDimension;
    private final Vector2 startIndex;
    private final Vector2 endIndex;

    private final Texture slotTexture;
    private final Texture platformTexture;
    private final Texture targetTexture;

    private FieldSlot[][] slots;

    private Rectangle fieldRectangle;

    private HashMap<SpriteObject, Vector2> objectPositions = new HashMap<SpriteObject, Vector2>();

    private int turretCount = 0;

    public Field(Dimension fieldDimension, Dimension slotDimension, Vector2 startIndex, Vector2 endIndex) {
        this.clickContainer = new ClickContainer(this);

        this.fieldDimension = fieldDimension;
        this.slotDimension = slotDimension;
        this.startIndex = startIndex;
        this.endIndex = endIndex;

        this.slotTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.GRASS_IMG);
        this.platformTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.PLATFORM_IMG);
        this.targetTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.TARGET_IMG);

        this.setDimension(new Dimension(this.fieldDimension.width * this.slotDimension.width, this.fieldDimension.height * this.slotDimension.height));

        this.slots = this.preparePlatforms(this.fieldDimension.width, this.fieldDimension.height);

        this.updateFieldRectangle();
    }

    private FieldSlot[][] preparePlatforms(int width, int height) {
        FieldSlot[][] slots = new FieldSlot[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[y][x] = new FieldSlot(x, y, y * width + x);
            }
        }

        return slots;
    }
    public boolean registerObject(SpriteObject object) {
        if (this.containsCoords(object.getPosition())) {
            Vector2 index = this.getIndexForCoords(object.getPosition());

            if (this.objectPositions.containsKey(object)) {
                Vector2 oldIndex = this.objectPositions.get(object);
                this.getSlotForIndex(oldIndex).removeObject(object);
            }

            objectPositions.put(object, index);
            this.getSlotForIndex(index).addObject(object);

            return true;
        }
        else if (this.objectPositions.containsKey(object)) {
            Vector2 oldIndex = this.objectPositions.get(object);
            this.getSlotForIndex(oldIndex).removeObject(object);
        }

        return false;
    }

    public boolean containsCoords(Vector2 coords) {
        return this.fieldRectangle.contains(coords) &&
               coords.x < this.fieldRectangle.x + this.fieldRectangle.width &&
               coords.y < this.fieldRectangle.y + this.fieldRectangle.height;
    }
    public boolean containsIndex(int x, int y) {
        return  x >= 0 && x < this.fieldDimension.width &&
                y >= 0 && y < this.fieldDimension.height;
    }

    public Vector2 getIndexForCoords(Vector2 coords) {
        coords.sub(this.getPosition()); // normalized position

        int x = (int) coords.x / (int) this.slotDimension.getWidth();
        int y = (int) coords.y / (int) this.slotDimension.getHeight();

        return new Vector2(x, y);
    }

    public Vector2 getSlotCoordinates(FieldSlot slot) {
        int x = (int) (slot.getPosition().x * this.slotDimension.width);
        int y = (int) (slot.getPosition().y * this.slotDimension.height);

        Vector2 coords = new Vector2(x, y);
        coords.add(this.getPosition());
        coords.add(this.slotDimension.width / 2, this.slotDimension.height / 2);

        return coords;
    }

    public FieldSlot getSlotForIndex(Vector2 slotIndex) {
        return this.slots[(int) slotIndex.y][(int) slotIndex.x];
    }
    public FieldSlot getSlotForIndex(int slotIndex) {
        return this.slots[slotIndex / this.fieldDimension.width][slotIndex % this.fieldDimension.width];
    }
    public FieldSlot getSlotForObject(SpriteObject object) {
        if (this.objectPositions.containsKey(object)) {
            return this.getSlotForIndex(this.objectPositions.get(object));
        }
        else return null;
    }

    public Dimension getFieldDimension() {
        return this.fieldDimension;
    }

    public FieldSlot getStartSlot() {
        return this.getSlotForIndex(this.startIndex);
    }
    public FieldSlot getEndSlot() {
        return this.getSlotForIndex(this.endIndex);
    }

    public int getTurretCount() {
        return this.turretCount;
    }

    private void updateFieldRectangle() {
        int marginX = (int) (this.fieldDimension.getWidth() * this.slotDimension.getWidth());
        int marginY = (int) (this.fieldDimension.getHeight() * this.slotDimension.getHeight());

        Vector2 anchorPoint = this.getPosition();

        this.fieldRectangle = new Rectangle(anchorPoint.x, anchorPoint.y, marginX, marginY);
    }

    /**
     * Recalculates the costs from the given turret.
     * @param turret last inserted turret
     */
    private void recalculateCosts(Turret turret) {
        for (int y = 0; y < this.fieldDimension.height; y++) {
            for (int x = 0; x < this.fieldDimension.width; x++) {
                FieldSlot slot = this.getSlotForIndex(y * this.fieldDimension.width + x);

                if (this.getSlotCoordinates(slot).dst(turret.getPosition()) <= turret.getRange()) {
                    slot.setCost(slot.getCost() + 10); // TODO set turret's real cost
                }
            }
        }
    }

    public void resetCosts() {
        for (int y = 0; y < this.getFieldDimension().height; y++) {
            for (int x = 0; x < this.getFieldDimension().width; x++) {
                slots[y][x].setCost(1);
            }
        }
    }

    @Override
    public void render(Batch batch, Camera camera) {
        Vector2 anchorPoint = this.getPosition();

        for (int y = 0; y < fieldDimension.getHeight(); y++) {
            for (int x = 0; x < fieldDimension.getWidth(); x++) {
                double xPos = anchorPoint.x + x * slotDimension.getWidth();
                double yPos = anchorPoint.y + y * slotDimension.getHeight();

                if (this.slots[y][x] == this.getEndSlot() || this.slots[y][x] == this.getStartSlot()) {
                    batch.draw(this.targetTexture, (float) xPos, (float) yPos);
                }
                else if (this.slots[y][x].isPlatform()) {
                    batch.draw(this.platformTexture, (float) xPos, (float) yPos);
                }
                else batch.draw(this.slotTexture, (float) xPos, (float) yPos);
            }
        }
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(this.getPosition().x, this.getPosition().y, this.getDimension().width, this.getDimension().height);
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
    public void onClick(MouseState mouseState) {
        GameScreen scr = (GameScreen) Locator.getGame().getActiveScreen();

        if (scr.getSelectedTurret() != null) {
            Turret turret = scr.getSelectedTurret();

            Vector2 index = this.getIndexForCoords(turret.getPosition());

            if (this.getSlotForIndex(index).isPlatform() && this.getSlotForIndex(index).isEmpty()) {
                if (this.registerObject(turret)) {
                    scr.onTurretSpawned(turret);

                    Vector2 coords = this.getSlotCoordinates(this.getSlotForIndex(index));

                    turret.setPosition(coords);

                    this.turretCount++;
                    this.recalculateCosts(turret);
                }
            }
        }
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);

        this.updateFieldRectangle();
    }
}
