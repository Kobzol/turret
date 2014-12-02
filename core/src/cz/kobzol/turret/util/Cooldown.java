package cz.kobzol.turret.util;

/**
 * Represents time cooldown in milliseconds.
 */
public class Cooldown implements Cloneable {
    private long duration_ms;
    private double elapsed_s;

    public Cooldown(long duration_ms) {
        this.duration_ms = duration_ms;
        this.elapsed_s = 0.0;
    }

    public boolean isReady() {
        return (this.elapsed_s * 1000.0) > this.duration_ms;
    }

    /**
     * Resets the cooldown if it is ready and returns whether it was resetted or not.
     * @return True if the cooldown was ready
     */
    public boolean resetIfReady() {
        boolean ready = this.isReady();

        if (ready) {
            this.reset();
        }

        return ready;
    }

    public void reset() {
        this.elapsed_s = 0.0;
    }

    public long getDuration() {
        return this.duration_ms;
    }
    public void setDuration(long duration_ms) {
        this.duration_ms = duration_ms;
    }

    public void update(float delta) {
        this.elapsed_s += delta;
    }
}
