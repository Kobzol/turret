package cz.kobzol.turret.model;

/**
 * Slot on a field of objects.
 */
public class FieldSlot {
    private final int x;
    private final int y;

    private boolean platform = false;

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
}
