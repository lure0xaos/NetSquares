package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefStringProperty(pkg: KClass<*>, key: String, defaultValue: String) :
    BasePrefProperty<String>(pkg, key, defaultValue) {
    override fun _set(value: String) {
        preferences.put(key, value)
    }

    override fun get(defaultValue: String): String {
        return preferences[key, defaultValue]
    }
}