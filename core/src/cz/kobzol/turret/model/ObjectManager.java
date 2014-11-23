package cz.kobzol.turret.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectManager<T extends GameObject>
{
    protected List<T> objects;
    protected HashMap<Integer, T> objectsId;
    protected HashMap<String, T> objectsKey;

    public ObjectManager() {
        this.objects = new ArrayList<T>();
        this.objectsId = new HashMap<Integer, T>();
        this.objectsKey = new HashMap<String, T>();
    }

    /**
     * Return registered objects
     */
    public ArrayList<T> getObjects() {
        return new ArrayList<T>(this.objects);
    }


    /**
     * Find object by his id
     * @param id
     * @return return object by generic or {@code null}
     */
    public T getObjectById(int id) {
        return objectsId.get(id);
    }


    /**
     * Find object by his key (key is no registered)
     * Implementation: If some object is renamed, ObjectManager try find them *only* based on new key (like foreach object).
     * @param key
     * @return return object by generic or {@code null}
     */
    public T getObjectByKey(String key) {
        if (objectsKey.containsKey(key)) {
            T object = objectsKey.get(key);
            if (object.getKey().equals(key)) {
                return object;
            } else {
                objectsKey.remove(key);
                objectsKey.put(object.getKey(), object);
            }
        }
        for (T object : objects) {
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
    public void registerObject(T object)
    {
        this.objects.add(object);
        this.objectsId.put(object.getId(), object);
        this.objectsKey.put(object.getKey(), object);
    }


    /**
     * Unregister object (second), and call event (first)
     * @param object
     */
    public void removeObject(T object) {
        this.objects.remove(object);
        this.objectsId.remove(object.getId());
        this.objectsKey.remove(object.getKey());
    }
}
