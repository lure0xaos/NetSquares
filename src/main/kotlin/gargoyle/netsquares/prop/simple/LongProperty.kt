package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class LongProperty(value: Long = 0) : RWProperty<Long>(value), Comparable<LongProperty> {

    override fun compareTo(other: LongProperty): Int {
        return get().toDouble().compareTo(other.get().toDouble())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return get() == other.get()
    }

    var value: Long
        get() = get()
        set(value) {
            set(value)
        }

    override fun hashCode(): Int = get().hashCode()
}
