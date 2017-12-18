package gargoyle.netsquares.prop.events;

import gargoyle.netsquares.prop.base.BaseRWProperty;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PropertyEvents<T> {
    private final BaseRWProperty<T> baseProperty;
    private final Set<PropertyListener<T>> listeners = new HashSet<>();

    public PropertyEvents(BaseRWProperty<T> baseProperty) {
        this.baseProperty = baseProperty;
    }

    public final void addPropertyListener(PropertyListener<T> listener) {
        listeners.add(listener);
    }

    public final void bind(BaseRWProperty<T> property) {
        property.addPropertyListener(baseProperty);
        baseProperty.addPropertyListener(property);
    }

    public final void bindTo(BaseRWProperty<T> property) {
        property.addPropertyListener(baseProperty);
    }

    public final void firePropertyChange(T oldValue, T newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            for (PropertyListener<T> listener : new ArrayList<>(listeners)) {
                if (listener != baseProperty) {
                    SwingUtilities.invokeLater(() -> listener.onPropertyChange(baseProperty, oldValue, newValue));
                }
            }
        }
    }

    public final void removePropertyListener(PropertyListener<T> listener) {
        listeners.remove(listener);
    }

    public final void removePropertyListeners() {
        listeners.clear();
    }

    @Override
    public String toString() {
        return String.format("PropertyEvents{baseProperty=%s}", baseProperty);
    }
}
