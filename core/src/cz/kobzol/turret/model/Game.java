package cz.kobzol.turret.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game.
 */
public class Game {
    private List<GameObject> objects;

    public Game() {
        this.objects = new ArrayList<GameObject>();
    }

    public List<GameObject> getObjects() {
        return new ArrayList<GameObject>(this.objects);
    }

    public void addObject(GameObject object) {
        this.objects.add(object);
    }
}
