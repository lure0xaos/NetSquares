package gargoyle.netsquares.store.prop;

import gargoyle.netsquares.util.SerializationUtils;

import java.io.Serializable;

public class PrefProperty<T extends Serializable> extends BasePrefProperty<T> {
    public PrefProperty(Class<?> pkg, String key, T defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(T value) {
        preferences.putByteArray(key, SerializationUtils.serialize(value));
    }

    public T get(T defaultValue) {
        byte[] bytes = preferences.getByteArray(key, null);
        return bytes == null ? defaultValue : SerializationUtils.deserialize(bytes);
    }
}
