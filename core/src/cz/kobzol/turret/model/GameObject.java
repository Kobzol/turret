package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.IGameObject;

/**
 * Represents game object with unique ID and key.
 */
public abstract class GameObject implements IGameObject {
    private static int id_counter = 0;

    private final int id;
    private String key;

    public GameObject() {
        this.id = GameObject.id_counter++;
        this.key = "object_" + this.id;
    }

    public final int getId() {
        return this.id;
    }
    public final String getKey() {
        return this.key;
    }
    public final void setKey(String key) {
        this.key = key;
    }
}
