package gargoyle.netsquares.prop;

import gargoyle.netsquares.prop.base.BaseRWProperty;

import java.util.Objects;

public class RWProperty<T> extends BaseRWProperty<T> {
    private T value;

    public RWProperty(T value) {
        this.value = value;
    }

    public RWProperty() {
    }

    @Override
    public final T get() {
        return value;
    }

    @Override
    protected final void _set(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWProperty)) return false;
        if (!super.equals(o)) return false;
        RWProperty<?> that = (RWProperty<?>) o;
        return Objects.equals(value, that.value);
    }
}
