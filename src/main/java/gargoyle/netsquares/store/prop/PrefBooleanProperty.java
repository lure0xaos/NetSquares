package gargoyle.netsquares.store.prop;

public class PrefBooleanProperty extends BasePrefProperty<Boolean> {
    public PrefBooleanProperty(Class<?> pkg, String key, Boolean defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(Boolean value) {
        preferences.putBoolean(key, value);
    }

    public Boolean get(Boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void setValue(boolean value) {
        set(value);
    }
}
