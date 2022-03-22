package gargoyle.netsquares.store.prop

import kotlin.reflect.KClass

class PrefDoubleProperty(pkg: KClass<*>, key: String, defaultValue: Double) :
    BasePrefProperty<Double>(pkg, key, defaultValue) {
    override fun _set(value: Double) {
        preferences.putDouble(key, value)
    }

    override fun get(defaultValue: Double): Double {
        return preferences.getDouble(key, defaultValue)
    }

    fun setValue(value: Double) {
        set(value)
    }
}
