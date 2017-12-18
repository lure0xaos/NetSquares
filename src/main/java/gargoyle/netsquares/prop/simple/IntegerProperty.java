package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public final class IntegerProperty extends RWProperty<Integer> implements Comparable<IntegerProperty>, Serializable {
    public IntegerProperty(Integer value) {
        super(value);
    }

    public IntegerProperty(int value) {
        super(value);
    }

    public IntegerProperty() {
        this(0);
    }

    @Override
    public int compareTo(@NotNull IntegerProperty o) {
        return Integer.compare(get(), o.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWProperty)) return false;
        if (!super.equals(o)) return false;
        RWProperty<?> that = (RWProperty<?>) o;
        return Objects.equals(get(), that.get());
    }

    public int getValue() {
        return get();
    }

    public void setValue(int value) {
        set(value);
    }
}
