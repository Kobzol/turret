package cz.kobzol.turret.model;

/**
 * Represents game object with unique ID and key.
 */
public abstract class GameObject implements Cloneable {
    private static int id_counter = 0;
    private static int generateID() {
        return GameObject.id_counter++;
    }

    private int id;
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

    @Override
    public Object clone() {
        try {
            GameObject obj = (GameObject) super.clone();
            obj.id = GameObject.generateID();

            return obj;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
