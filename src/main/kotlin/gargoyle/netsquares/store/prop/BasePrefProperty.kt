package gargoyle.netsquares.store.prop

import gargoyle.netsquares.prop.base.BaseRWProperty
import java.util.prefs.Preferences
import kotlin.reflect.KClass

abstract class BasePrefProperty<T : Any> protected constructor(pkg: KClass<*>, key: String, defaultValue: T) :
    BaseRWProperty<T>() {
    protected val key: String
    protected val preferences: Preferences

    private val defaultValue: T

    init {
        this.key = key
        preferences = Preferences.userNodeForPackage(pkg.java)
        this.defaultValue = defaultValue
    }

    override fun flush() {
        try {
            preferences.sync()
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    override fun get(): T {
        return get(defaultValue)
    }

    abstract operator fun get(defaultValue: T): T
    val value: T
        get() {
            return get(defaultValue)
        }
}
