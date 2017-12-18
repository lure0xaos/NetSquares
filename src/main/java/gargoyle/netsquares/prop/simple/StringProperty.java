package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;

import java.io.Serializable;
import java.util.Objects;

public final class StringProperty extends RWProperty<String> implements Comparable<StringProperty>, Serializable {
    public StringProperty() {
        this("");
    }

    public StringProperty(String value) {
        super(value);
    }

    @Override
    public int compareTo(StringProperty o) {
        String thisValue = get();
        String thatValue = o.get();
        return (thisValue == null ? "" : thisValue).compareTo(thatValue == null ? "" : thatValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RWProperty)) return false;
        if (!super.equals(o)) return false;
        RWProperty<?> that = (RWProperty<?>) o;
        return Objects.equals(get(), that.get());
    }
}
