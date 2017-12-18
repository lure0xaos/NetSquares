package gargoyle.netsquares.util;

import gargoyle.netsquares.prop.base.BaseRWProperty;

import javax.swing.AbstractButton;

public final class SwingProperties {
    private SwingProperties() {
    }

    public static void bind(BaseRWProperty<Boolean> property, AbstractButton item) {
        item.setSelected(property.get());
        item.addActionListener(e -> property.set(item.isSelected()));
        property.addPropertyListener((prop, oldValue, newValue) -> item.setSelected(newValue));
    }
}
