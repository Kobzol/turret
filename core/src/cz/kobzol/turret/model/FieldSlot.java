package cz.kobzol.turret.model;

import com.badlogic.gdx.math.Vector2;
import cz.kobzol.turret.graphics.SpriteObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Slot on a field of objects.
 */
public class FieldSlot {
    private final Vector2 position;
    private final int index;

    private boolean platform = false;

    private final List<SpriteObject> objects = new ArrayList<SpriteObject>();

    public FieldSlot(int x, int y, int index) {
        this.position = new Vector2(x, y);
        this.index = index;
    }

    public Vector2 getPosition() {
        return this.position.cpy();
    }
    public int getIndex() {
        return this.index;
    }

    public void setPlatform(boolean platform) {
        this.platform = platform;
    }
    public boolean isPlatform() {
        return this.platform;
    }

    public boolean isEmpty() {
        return this.objects.size() == 0;
    }

    public void addObject(SpriteObject object) {
        if (!this.objects.contains(object)) {
            this.objects.add(object);
        }
    }
    public void removeObject(SpriteObject object) {
        this.objects.remove(object);
    }
    public boolean containsObject(SpriteObject object) {
        return this.objects.contains(object);
    }
}
