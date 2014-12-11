package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.IUpdatable;
import cz.kobzol.turret.util.Cooldown;

import java.util.ArrayList;
import java.util.List;

/**
 * Spawns waves of objects.
 */
public class WaveSpawner implements IUpdatable {
    private final List<SpawnListener> listeners = new ArrayList<SpawnListener>();

    private final List<Wave> waves = new ArrayList<Wave>();
    private final Cooldown cooldown = new Cooldown(1000);

    private int waveIndex = 0;
    private boolean spawning = false;

    public void addWave(Wave wave) {
        this.waves.add(wave);
    }
    public void addListener(SpawnListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void update(float delta) {
        if (!spawning) {
            return;
        }

        if (this.waveIndex == 0) {
            this.cooldown.setDuration(this.waves.get(this.waveIndex).getSpawnCooldown());
        }

        this.cooldown.update(delta);

        if (this.cooldown.resetIfReady()) {
            if (this.waves.get(this.waveIndex).hasObject()) {
                GameObject object = (GameObject) this.waves.get(this.waveIndex).getObject().clone();
                this.notifyObjectSpawned(object);
            }

            this.spawning = this.waves.get(this.waveIndex).hasObject();

            if (!spawning) {
                this.notifyWaveEnded();
                this.waveIndex++;

                if (this.hasWaves()) {
                    this.cooldown.setDuration(this.waves.get(waveIndex).getSpawnCooldown());
                }
            }
        }
    }

    public boolean hasWaves() {
        return this.waveIndex < this.waves.size();
    }

    public void startSpawning() {
        this.spawning = true;
    }

    private void notifyObjectSpawned(GameObject object) {
        for (SpawnListener listener : this.listeners) {
            listener.onObjectSpawned(object);
        }
    }
    private void notifyWaveEnded() {
        for (SpawnListener listener : this.listeners) {
            listener.onWaveEnded();
        }
    }

    public static interface SpawnListener {
        void onObjectSpawned(GameObject object);
        void onWaveEnded();
    }
}
