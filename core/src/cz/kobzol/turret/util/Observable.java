package cz.kobzol.turret.util;

/**
 * Observable objects.
 */
public interface Observable {
    public static interface ObservableListener {
        void onRemove(Observable observable);
    }

    void addListener(ObservableListener listener);
}
