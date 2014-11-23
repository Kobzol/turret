package cz.kobzol.turret.util;

import cz.kobzol.turret.model.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectManager
{
    protected List<GameObject> objects;
    protected HashMap<Integer, GameObject> objectsId;
    protected HashMap<String, GameObject> objectsKey;

    public ObjectManager() {
        this.objects = new ArrayList<GameObject>();
        this.objectsId = new HashMap<Integer, GameObject>();
        this.objectsKey = new HashMap<String, GameObject>();
    }

    /**
     * Return registered objects
     */
    public ArrayList<GameObject> getObjects() {
        return (ArrayList<GameObject>) this.objects;
    }


    /**
     * Find object by his id
     * @param id
     * @return return object by generic or {@code null}
     */
    public GameObject getObjectById(int id) {
        return objectsId.get(id);
    }


    /**
     * Find object by his key (key is no registered)
     * Implementation: If some object is renamed, ObjectManager try find them *only* based on new key (like foreach object).
     * @param key
     * @return return object by generic or {@code null}
     */
    public GameObject getObjectByKey(String key) {
        if (objectsKey.containsKey(key)) {
            GameObject object = objectsKey.get(key);
            if (object.getKey().equals(key)) {
                return object;
            } else {
                objectsKey.remove(key);
                objectsKey.put(object.getKey(), object);
            }
        }
        for (GameObject object : objects) {
            if (object.getKey().equals(key)) {
                objectsKey.put(object.getKey(), object);
                return object;
            }
        }
        return null;
    }


    /**
     * Register new object (first) and call event (second)
     * @param object
     */
    public void registerObject(GameObject object)
    {
        this.objects.add(object);
        this.objectsId.put(object.getId(), object);
        this.objectsKey.put(object.getKey(), object);
    }


    /**
     * Unregister object (second), and call event (first)
     * @param object
     */
    public void removeObject(GameObject object) {
        this.objects.remove(object);
        this.objectsId.remove(object.getId());
        this.objectsKey.remove(object.getKey());
    }
}
