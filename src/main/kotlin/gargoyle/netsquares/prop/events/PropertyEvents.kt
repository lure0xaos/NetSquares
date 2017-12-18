package gargoyle.netsquares.prop.events

import gargoyle.netsquares.prop.base.BaseRWProperty
import javax.swing.SwingUtilities

class PropertyEvents<T : Any>(baseProperty: BaseRWProperty<T>) {
    private val baseProperty: BaseRWProperty<T>
    private val listeners: MutableSet<PropertyListener<T>> = mutableSetOf()

    init {
        this.baseProperty = baseProperty
    }

    fun addPropertyListener(listener: PropertyListener<T>) {
        listeners.add(listener)
    }

    fun bind(property: BaseRWProperty<T>) {
        property.addPropertyListener(baseProperty)
        baseProperty.addPropertyListener(property)
    }

    fun bindTo(property: BaseRWProperty<T>) {
        property.addPropertyListener(baseProperty)
    }

    fun firePropertyChange(oldValue: T, newValue: T) {
        if (oldValue != newValue) {
            for (listener in ArrayList(listeners)) {
                if (listener !== baseProperty) {
                    SwingUtilities.invokeLater { listener.onPropertyChange(baseProperty, oldValue, newValue) }
                }
            }
        }
    }

    fun removePropertyListener(listener: PropertyListener<T>) {
        listeners.remove(listener)
    }

    fun removePropertyListeners() {
        listeners.clear()
    }

    override fun toString(): String {
        return String.format("PropertyEvents{baseProperty=%s}", baseProperty)
    }
}
