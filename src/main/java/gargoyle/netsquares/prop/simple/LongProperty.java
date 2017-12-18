package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;

import java.io.Serializable;
import java.util.Objects;

public final class LongProperty extends RWProperty<Long> implements Comparable<LongProperty>, Serializable {
    public LongProperty(Long value) {
        super(value);
    }

    public LongProperty() {
        this(0);
    }

    public LongProperty(long value) {
        super(value);
    }

    @Override
    public int compareTo(LongProperty o) {
        return Double.compare(get(), o.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWProperty)) return false;
        if (!super.equals(o)) return false;
        RWProperty<?> that = (RWProperty<?>) o;
        return Objects.equals(get(), that.get());
    }

    public long getValue() {
        return get();
    }

    public void setValue(long value) {
        set(value);
    }
}
