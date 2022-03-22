package gargoyle.netsquares.util

import gargoyle.netsquares.prop.base.BaseRWProperty
import javax.swing.AbstractButton

object SwingProperties {
    fun bind(property: BaseRWProperty<Boolean>, item: AbstractButton) {
        item.isSelected = property.get()
        item.addActionListener { property.set(item.isSelected) }
        property.addPropertyListener { _: BaseRWProperty<Boolean>, _: Boolean, newValue: Boolean ->
            item.isSelected = newValue
        }
    }
}
