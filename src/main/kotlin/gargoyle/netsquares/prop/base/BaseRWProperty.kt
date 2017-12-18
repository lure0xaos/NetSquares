package gargoyle.netsquares.prop.base

import gargoyle.netsquares.prop.events.PropertyEvents
import gargoyle.netsquares.prop.events.PropertyListener

abstract class BaseRWProperty<T : Any> protected constructor() : BaseROProperty<T>(), IRWProperty<T>,
    PropertyListener<T> {
    private val events = PropertyEvents(this)
    fun addPropertyListener(listener: PropertyListener<T>) {
        events.addPropertyListener(listener)
    }

    fun bind(property: BaseRWProperty<T>) {
        events.bind(property)
    }

    fun bindTo(property: BaseRWProperty<T>) {
        events.bindTo(property)
    }

    override fun onPropertyChange(property: BaseRWProperty<T>, oldValue: T, newValue: T) {
        set(newValue)
    }

    override fun set(value: T) {
        val oldValue = get()
        _set(value)
        try {
            flush()
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
        events.firePropertyChange(oldValue, value)
    }

    protected abstract fun _set(value: T)
    protected open fun flush() {
    }

    fun removePropertyListener(listener: PropertyListener<T>) {
        events.removePropertyListener(listener)
    }

    fun removePropertyListeners() {
        events.removePropertyListeners()
    }
}
