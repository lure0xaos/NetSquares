package gargoyle.netsquares.store.prop

import gargoyle.netsquares.prop.base.BaseRWProperty
import java.util.prefs.Preferences
import kotlin.reflect.KClass

abstract class BasePrefProperty<T : Any> protected constructor(
    pkg: KClass<*>,
    protected val key: String,
    private val defaultValue: T
) : BaseRWProperty<T>() {
    protected val preferences: Preferences = Preferences.userNodeForPackage(pkg.java)

    override fun flush() {
        try {
            preferences.sync()
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    override fun get(): T = get(defaultValue)

    abstract operator fun get(defaultValue: T): T
    val value: T get() = get(defaultValue)
}
