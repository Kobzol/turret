package cz.kobzol.turret.util;

/**
 * Represents time cooldown
 */
public class Cooldown {
    private final long interval;
    private long value;

    public Cooldown(long interval) {
        this.interval = interval;
    }

    public boolean isReady() {
        return this.value > this.interval;
    }
    public void reset() {
        this.value = 0;
    }

    public void update() {
        this.value++;
    }
}
