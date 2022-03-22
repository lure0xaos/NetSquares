package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class StringProperty constructor(value: String = "") : RWProperty<String>(value),
    Comparable<StringProperty> {
    override fun compareTo(other: StringProperty): Int {
        val thisValue = get()
        val thatValue = other.get()
        return thisValue.compareTo(thatValue)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return get() == other.get()
    }

    override fun hashCode(): Int = get().hashCode()
}
