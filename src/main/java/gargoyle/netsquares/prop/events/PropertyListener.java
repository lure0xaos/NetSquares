package gargoyle.netsquares.prop.events;

import gargoyle.netsquares.prop.base.BaseRWProperty;

@FunctionalInterface
public interface PropertyListener<T> {
    void onPropertyChange(BaseRWProperty<T> property, T oldValue, T newValue);
}
