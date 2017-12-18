package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;

import java.io.Serializable;
import java.util.Objects;

public final class DoubleProperty extends RWProperty<Double> implements Comparable<DoubleProperty>, Serializable {
    public DoubleProperty(Double value) {
        super(value);
    }

    public DoubleProperty() {
        this(0);
    }

    public DoubleProperty(double value) {
        super(value);
    }

    @Override
    public int compareTo(DoubleProperty o) {
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

    public double getValue() {
        return get();
    }

    public void setValue(double value) {
        set(value);
    }
}
