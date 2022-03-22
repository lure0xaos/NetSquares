package gargoyle.netsquares.prop

import gargoyle.netsquares.prop.base.BaseRWProperty

open class RWProperty<T : Any> : BaseRWProperty<T> {
    private lateinit var value: T

    constructor(value: T) {
        this.value = value
    }

    constructor()

    override fun _set(value: T) {
        this.value = value
    }

    override fun hashCode(): Int {
        return arrayOf(super.hashCode(), value).contentHashCode()
    }

    override fun get(): T {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return value == other.value
    }
}
