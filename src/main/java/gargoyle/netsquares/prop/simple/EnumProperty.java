package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public final class EnumProperty<E extends Enum<E>> extends RWProperty<E> implements Comparable<EnumProperty<E>>, Serializable {
    public EnumProperty(E value) {
        super(value);
    }

    @Override
    public int compareTo(@NotNull EnumProperty<E> o) {
        return Integer.compare(get().ordinal(), o.get().ordinal());
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
