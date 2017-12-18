package gargoyle.netsquares.store.prop;

public class PrefEnumProperty<E extends Enum<E>> extends BasePrefProperty<E> {
    public PrefEnumProperty(Class<?> pkg, String key, E defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(E value) {
        preferences.put(key, (value).name());
    }

    public E get(E defaultValue) {
        String name = preferences.get(key, null);
        return name == null ? defaultValue : Enum.valueOf(defaultValue.getDeclaringClass(), name);
    }
}
