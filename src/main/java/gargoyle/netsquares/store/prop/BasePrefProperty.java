package gargoyle.netsquares.store.prop;

import gargoyle.netsquares.prop.base.BaseRWProperty;

import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public abstract class BasePrefProperty<T> extends BaseRWProperty<T> {
    protected final String key;
    protected final Preferences preferences;
    private final transient T defaultValue;

    protected BasePrefProperty(Class<?> pkg, String key, T defaultValue) {
        this.key = Objects.requireNonNull(key);
        preferences = Preferences.userNodeForPackage(pkg);
        this.defaultValue = defaultValue;
    }

    protected final void flush() {
        try {
            preferences.sync();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public abstract T get(T defaultValue);

    public final T get() {
        return get(defaultValue);
    }

    public T getValue() {
        T t = get(defaultValue);
        return t == null ? nonNull() : t;
    }

    @SuppressWarnings("unchecked")
    private T nonNull() {
        try {
            return ((Class<T>) defaultValue.getClass()).getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
