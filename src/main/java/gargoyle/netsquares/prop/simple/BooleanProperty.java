package gargoyle.netsquares.prop.simple;

import gargoyle.netsquares.prop.RWProperty;

import java.io.Serializable;

public final class BooleanProperty extends RWProperty<Boolean> implements Serializable {
    public BooleanProperty(Boolean value) {
        super(value);
    }

    public BooleanProperty(boolean value) {
        super(value);
    }

    public BooleanProperty() {
        this(false);
    }

    public boolean getValue() {
        return get();
    }

    public void setValue(boolean value) {
        set(value);
    }
}
