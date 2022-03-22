package gargoyle.netsquares.prop.base

interface IRWProperty<T : Any> : IROProperty<T> {
    fun set(value: T)
}
