package cz.kobzol.turret.map;

import cz.kobzol.turret.model.GameObject;
import cz.kobzol.turret.model.ObjectManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents game level with game objects.
 */
public class Level {
    private final ObjectManager<GameObject> manager;

    public Level() {
        this.manager = new ObjectManager<GameObject>();
    }

    public GameObject getObjectById(int id) {
        return this.manager.getObjectById(id);
    }
    public GameObject getObjectByKey(String key) {
        return this.manager.getObjectByKey(key);
    }

    public void addObject(GameObject object) {
        this.manager.registerObject(object);
    }
}
