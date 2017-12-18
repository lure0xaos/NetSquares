package gargoyle.netsquares.prop.base

abstract class BaseROProperty<T : Any> protected constructor() : IROProperty<T> {
    override fun hashCode(): Int {
        return get().hashCode()
    }

    abstract override fun get(): T
    override fun equals(other: Any?): Boolean {
        return this === other || other is IROProperty<*> && get() == other.get()
    }

    override fun toString(): String {
        return String.format("%s(%s)", javaClass.name, get())
    }
}
