package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefEnumProperty<E : Enum<E>>(pkg: KClass<*>, key: String, defaultValue: E) :
    BasePrefProperty<E>(pkg, key, defaultValue) {
    override fun _set(value: E) {
        preferences.put(key, value.name)
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(defaultValue: E): E {
        val name = preferences[key, null]
        return if (name == null) defaultValue
        else {
            java.lang.Enum.valueOf(defaultValue.javaClass.declaringClass as Class<E>, name)
        }
    }
}
