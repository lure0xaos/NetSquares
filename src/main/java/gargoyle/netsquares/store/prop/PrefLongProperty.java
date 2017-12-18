package gargoyle.netsquares.store.prop;

public class PrefLongProperty extends BasePrefProperty<Long> {
    public PrefLongProperty(Class<?> pkg, String key, Long defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(Long value) {
        preferences.putLong(key, value);
    }

    public Long get(Long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public void setValue(long value) {
        set(value);
    }
}
