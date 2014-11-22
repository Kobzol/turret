package cz.kobzol.turret.model;

import cz.kobzol.turret.graphics.IGameObject;

/**
 * Represents game object with unique ID.
 */
public abstract class GameObject implements IGameObject {
    private static int id_counter = 0;

    private final int id;

    public GameObject() {
        this.id = GameObject.id_counter++;
    }

    final public int getId() {
        return this.id;
    }
}
