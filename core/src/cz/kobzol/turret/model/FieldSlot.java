package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.SpriteObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Slot on a field of objects.
 */
public class FieldSlot {
    private final int x;
    private final int y;

    private boolean platform = false;

    private final List<SpriteObject> objects = new ArrayList<SpriteObject>();

    public FieldSlot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void markAsPlatform() {
        this.platform = true;
    }
    public boolean isPlatform() {
        return this.platform;
    }

    public void addObject(SpriteObject object) {
        if (!this.objects.contains(object)) {
            this.objects.add(object);
        }
    }
    public void removeObject(SpriteObject object) {
        this.objects.remove(object);
    }
}
