package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefIntegerProperty(pkg: KClass<*>, key: String, defaultValue: Int) :
    BasePrefProperty<Int>(pkg, key, defaultValue) {
    override fun _set(value: Int) {
        preferences.putInt(key, value)
    }

    override fun get(defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun setValue(value: Int) {
        set(value)
    }
}
