package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class EnumProperty<E : Enum<E>>(value: E) : RWProperty<E>(value), Comparable<EnumProperty<E>> {
    override fun compareTo(other: EnumProperty<E>): Int {
        return get().ordinal.compareTo(other.get().ordinal)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return get() == other.get()
    }

    override fun hashCode(): Int = get().hashCode()
}
