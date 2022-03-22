package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class DoubleProperty(value: Double = 0.0) : RWProperty<Double>(value), Comparable<DoubleProperty> {

    override fun compareTo(other: DoubleProperty): Int {
        return (get()).compareTo(other.get())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RWProperty<*>) return false
        if (!super.equals(other)) return false
        return get() == other.get()
    }

    var value: Double
        get() = get()
        set(value) {
            set(value)
        }

    override fun hashCode(): Int = get().hashCode()
}
