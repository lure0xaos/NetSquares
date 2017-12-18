package gargoyle.netsquares.store.prop;

public class PrefStringProperty extends BasePrefProperty<String> {
    public PrefStringProperty(Class<?> pkg, String key, String defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(String value) {
        preferences.put(key, value);
    }

    public String get(String defaultValue) {
        return preferences.get(key, defaultValue);
    }
}
