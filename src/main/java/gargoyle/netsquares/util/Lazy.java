package gargoyle.netsquares.util;

import java.util.function.Supplier;

public final class Lazy<T> implements Supplier<T> {
    private final Supplier<T> objectSupplier;
    private T object;

    public Lazy(Supplier<T> objectSupplier) {
        this.objectSupplier = objectSupplier;
    }

    @Override
    public synchronized T get() {
        if (object == null) {
            object = objectSupplier.get();
        }
        return object;
    }
}
