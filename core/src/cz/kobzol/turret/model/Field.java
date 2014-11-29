package cz.kobzol.turret.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.IDrawable;
import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.services.Locator;
import cz.kobzol.turret.util.AssetContainer;

import java.awt.Dimension;

/**
 * Field with demons and turrets.
 */
public class Field implements IDrawable, IUpdatable {
    private final Vector2 anchorPoint;

    private final Dimension dimension = new Dimension(52, 20);
    private final Dimension slotDimension = new Dimension(30, 30);

    private Texture slotTexture;
    private Texture platformTexture;

    private FieldSlot[][] slots;

    public Field(Vector2 position) {
        this.anchorPoint = new Vector2(position);

        this.slotTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.GRASS_IMG);
        this.platformTexture = Locator.getAssetContainer().getAssetManager().get(AssetContainer.PLATFORM_IMG);

        this.slots = this.preparePlatforms(this.dimension.width, this.dimension.height);
    }

    private FieldSlot[][] preparePlatforms(int width, int height) {
        FieldSlot[][] slots = new FieldSlot[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                slots[y][x] = new FieldSlot(x, y);
            }
        }

        slots[5][8].markAsPlatform();

        return slots;
    }

    @Override
    public void render(Batch batch, Camera camera) {
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
    public void update(float delta) {

    }
}
