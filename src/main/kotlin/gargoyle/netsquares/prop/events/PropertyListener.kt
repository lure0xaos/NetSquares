package gargoyle.netsquares.prop.events

import gargoyle.netsquares.prop.base.BaseRWProperty

fun interface PropertyListener<T : Any> {
    fun onPropertyChange(property: BaseRWProperty<T>, oldValue: T, newValue: T)
}
