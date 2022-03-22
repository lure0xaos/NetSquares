package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefLongProperty(pkg: KClass<*>, key: String, defaultValue: Long) :
    BasePrefProperty<Long>(pkg, key, defaultValue) {
    override fun _set(value: Long) {
        preferences.putLong(key, value)
    }

    override fun get(defaultValue: Long): Long {
        return preferences.getLong(key, defaultValue)
    }

    fun setValue(value: Long) {
        set(value)
    }
}
