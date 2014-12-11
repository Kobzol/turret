package cz.kobzol.turret.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wave of objects.
 */
public class Wave {
    private final List<GameObject> objects = new ArrayList<GameObject>();
    private final List<Integer> counts = new ArrayList<Integer>();

    private int objectIndex = 0;
    private final long spawnCooldown;

    public Wave(long spawnCooldown) {
        this.spawnCooldown = spawnCooldown;
    }

    public void addSpawnee(GameObject spawnee, int count) {
        assert(count > 0);
        assert(spawnee != null);

        this.objects.add(spawnee);
        this.counts.add(count);
    }

    public boolean hasObject() {
        return this.objectIndex < this.objects.size();
    }

    public GameObject getObject() {
        GameObject object = this.objects.get(this.objectIndex);

        this.counts.set(this.objectIndex, this.counts.get(this.objectIndex) - 1);

        if (this.counts.get(this.objectIndex) == 0) {
            this.objectIndex++;
        }

        return object;
    }

    public long getSpawnCooldown() {
        return this.spawnCooldown;
    }
}
