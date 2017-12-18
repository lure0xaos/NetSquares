package gargoyle.netsquares.util

import kotlin.reflect.KClass

object Maps {

    fun <K : Any?, V : Any> keyForValueMultiMapSetAny(map: Map<K, MutableSet<V>>, value: V): K? =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }.firstOrNull()

    fun <K : Any, V : Any> map(vararg data: Any): Map<K, V> = fill(mutableMapOf(), *data)

    @Suppress("UNCHECKED_CAST")
    private fun <K : Any, V : Any> fill(map: MutableMap<K, V>, vararg data: Any): Map<K, V> {
        var i = 0
        while (i < data.size) {
            map[data[i] as K] = data[i + 1] as V
            i += 2
        }
        return map
    }

    private fun <K : Any, V : Any> vs(
        map: MutableMap<K, MutableSet<V>>, key: K, listSource: () -> MutableSet<V>
    ): MutableSet<V> {
        return if (map[key] == null) {
            val newValue = listSource()
            map[key] = newValue
            newValue
        } else map[key]!!
    }

    fun <K : Any, V : Any> multiMapSet(
        keyType: KClass<K>, valueType: KClass<V>, vararg data: Any
    ): Map<K, MutableSet<V>> = multiMapSetFrom(keyType, valueType, { mutableMapOf() }, { mutableSetOf() }, *data)

    @Suppress("UNCHECKED_CAST")
    private fun <K : Any, V : Any> multiMapSetFrom(
        keyType: KClass<K>,
        valueType: KClass<V>,
        mapSource: () -> MutableMap<K, MutableSet<V>>,
        listSource: () -> MutableSet<V>,
        vararg data: Any
    ): Map<K, MutableSet<V>> {
        val map = mapSource()
        lateinit var key: K
        for (el in data) {
            when {
                keyType.isInstance(el) -> {
                    key = el as K
                }

                valueType.isInstance(el) -> {
                    vs(map, key) { listSource() }.add(el as V)
                }

                else -> {
                    error("incorrect")
                }
            }
        }
        return map
    }

}
