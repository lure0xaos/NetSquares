package gargoyle.netsquares.store.prop;

public class PrefIntegerProperty extends BasePrefProperty<Integer> {
    public PrefIntegerProperty(Class<?> pkg, String key, Integer defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(Integer value) {
        preferences.putInt(key, value);
    }

    public Integer get(Integer defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public void setValue(int value) {
        set(value);
    }
}
