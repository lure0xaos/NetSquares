package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class IntegerProperty(value: Int = 0) : RWProperty<Int>(value), Comparable<IntegerProperty> {

    override fun compareTo(other: IntegerProperty): Int {
        return get().compareTo(other.get())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return get() == other.get()
    }

    var value: Int
        get() = get()
        set(value) {
            set(value)
        }

    override fun hashCode(): Int = get().hashCode()
}
