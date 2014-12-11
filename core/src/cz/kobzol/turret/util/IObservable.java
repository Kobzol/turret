package cz.kobzol.turret.util;

/**
 * Observable objects.
 */
public interface IObservable {
    public static interface ObservableListener {
        void onRemove(IObservable observable);
    }

    void addListener(ObservableListener listener);
}
