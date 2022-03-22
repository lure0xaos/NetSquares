package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefBooleanProperty(pkg: KClass<*>, key: String, defaultValue: Boolean) :
    BasePrefProperty<Boolean>(pkg, key, defaultValue) {
    override fun _set(value: Boolean) {
        preferences.putBoolean(key, value)
    }

    override fun get(defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun setValue(value: Boolean) {
        set(value)
    }
}
