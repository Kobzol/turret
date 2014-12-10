package cz.kobzol.turret.util;

import java.util.ArrayList;
import java.util.List;

/**
 * List that observes it's containing items's deletion.
 */
public class ObservingList<T extends IObservable> extends ArrayList<T> {
    private List<IObservable> flaggedObjects = new ArrayList<IObservable>();
    private List<IObservable.ObservableListener> listeners = new ArrayList<IObservable.ObservableListener>();

    @Override
    public boolean add(T t) {
        t.addListener(new IObservable.ObservableListener() {
            @Override
            public void onRemove(IObservable observable) {
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

    private void notifyObjectRemoved(IObservable observable) {
        for (IObservable.ObservableListener listener : this.listeners) {
            listener.onRemove(observable);
        }
    }

    public void addListener(IObservable.ObservableListener listener) {
        this.listeners.add(listener);
    }
}
