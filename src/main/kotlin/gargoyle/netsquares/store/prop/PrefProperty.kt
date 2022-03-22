package gargoyle.netsquares.store.prop

import gargoyle.netsquares.util.SerializationUtils
import kotlin.reflect.KClass

class PrefProperty<T : Any>(pkg: KClass<*>, key: String, defaultValue: T) :
    BasePrefProperty<T>(pkg, key, defaultValue) {
    override fun _set(value: T) {
        preferences.putByteArray(key, SerializationUtils.serialize(value))
    }

    override fun get(defaultValue: T): T {
        val bytes = preferences.getByteArray(key, null)
        return if (bytes == null) defaultValue else SerializationUtils.deserialize(bytes)
    }
}
