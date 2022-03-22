package gargoyle.netsquares.prop

import gargoyle.netsquares.prop.base.BaseROProperty

class ROProperty<T : Any>(private val value: T) : BaseROProperty<T>() {
    override fun get(): T {
        return value
    }
}
