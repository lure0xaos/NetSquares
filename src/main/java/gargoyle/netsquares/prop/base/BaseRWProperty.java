package gargoyle.netsquares.prop.base;

import gargoyle.netsquares.prop.events.PropertyEvents;
import gargoyle.netsquares.prop.events.PropertyListener;

public abstract class BaseRWProperty<T> extends BaseROProperty<T> implements IRWProperty<T>, PropertyListener<T> {
    private final PropertyEvents<T> events = new PropertyEvents<>(this);

    protected BaseRWProperty() {
    }

    public final void addPropertyListener(PropertyListener<T> listener) {
        events.addPropertyListener(listener);
    }

    public final void bind(BaseRWProperty<T> property) {
        events.bind(property);
    }

    public final void bindTo(BaseRWProperty<T> property) {
        events.bindTo(property);
    }

    @Override
    public final void onPropertyChange(BaseRWProperty<T> property, T oldValue, T newValue) {
        set(newValue);
    }

    @Override
    public final void set(T value) {
        T oldValue = get();
        _set(value);
        try {
            flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        events.firePropertyChange(oldValue, value);
    }

    protected abstract void _set(T value);

    @SuppressWarnings("RedundantThrows")
    protected void flush() throws Exception {
    }

    public final void removePropertyListener(PropertyListener<T> listener) {
        events.removePropertyListener(listener);
    }

    public final void removePropertyListeners() {
        events.removePropertyListeners();
    }
}
