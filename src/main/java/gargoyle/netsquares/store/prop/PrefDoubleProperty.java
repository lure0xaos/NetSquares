package gargoyle.netsquares.store.prop;

public class PrefDoubleProperty extends BasePrefProperty<Double> {
    public PrefDoubleProperty(Class<?> pkg, String key, Double defaultValue) {
        super(pkg, key, defaultValue);
    }

    @Override
    protected void _set(Double value) {
        preferences.putDouble(key, value);
    }

    public Double get(Double defaultValue) {
        return preferences.getDouble(key, defaultValue);
    }

    public void setValue(double value) {
        set(value);
    }
}
