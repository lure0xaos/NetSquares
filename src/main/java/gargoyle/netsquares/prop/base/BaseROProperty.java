package gargoyle.netsquares.prop.base;

import java.util.Objects;

public abstract class BaseROProperty<T> implements IROProperty<T> {
    protected BaseROProperty() {
    }

    @Override
    public abstract T get();

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof IROProperty && Objects.equals(get(), ((IROProperty<?>) o).get());
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getName(), get());
    }
}
