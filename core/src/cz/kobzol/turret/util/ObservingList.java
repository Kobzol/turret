package cz.kobzol.turret.util;

import java.util.ArrayList;
import java.util.List;

/**
 * List that observes it's containing items's deletion.
 */
public class ObservingList<T extends Observable> extends ArrayList<T> {
    private List<Observable> flaggedObjects = new ArrayList<Observable>();
    private List<Observable.ObservableListener> listeners = new ArrayList<Observable.ObservableListener>();

    @Override
    public boolean add(T t) {
        t.addListener(new Observable.ObservableListener() {
            @Override
            public void onRemove(Observable observable) {
                flaggedObjects.add(observable);
                notifyObjectRemoved(observable);
            }
        });

        return super.add(t);
    }

    public void deleteFlaggedObjects() {
        if (this.flaggedObjects.size() > 0) {
            this.removeAll(flaggedObjects);
            this.flaggedObjects.clear();
        }
    }

    private void notifyObjectRemoved(Observable observable) {
        for (Observable.ObservableListener listener : this.listeners) {
            listener.onRemove(observable);
        }
    }

    public void addListener(Observable.ObservableListener listener) {
        this.listeners.add(listener);
    }
}
