package gargoyle.netsquares.prop;

import gargoyle.netsquares.prop.base.BaseROProperty;

public class ROProperty<T> extends BaseROProperty<T> {
    private final T value;

    public ROProperty(T value) {
        this.value = value;
    }

    @Override
    public final T get() {
        return value;
    }
}
