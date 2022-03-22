package gargoyle.netsquares.util

import kotlin.reflect.KClass

object Maps {
    fun <K : Any, V : Any> keyForValue(map: Map<K, V>, value: V): K? =
        map.filter { (_, value1) -> value1 == value }.map { (key, _) -> key }.firstOrNull()

    fun <K : Any, V : Any> keyForValueMultiMap(map: Map<K, List<V>>, value: V): K? =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }.firstOrNull()

    fun <K : Any, V : Any> keyForValueMultiMapset(map: Map<K, MutableSet<V>>, value: V): K? =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }.firstOrNull()

    fun <K : Any?, V : Any> keyForValueMultiMapsetAny(map: Map<K, MutableSet<V>>, value: V): K? =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }.firstOrNull()

    fun <K : Any, V : Any> keysForValue(map: Map<K, V>, value: V): List<K> =
        map.filter { (_, value1) -> value1 == value }.map { (key, _) -> key }

    fun <K : Any, V : Any> keysForValueMultiMap(map: Map<K, List<V>>, value: V): List<K> =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }

    fun <K : Any, V : Any> keysForValueMultiMapset(map: Map<K, Set<V>>, value: V): List<K> =
        map.filter { (_, value1) -> value1.contains(value) }.map { (key, _) -> key }

    fun <K : Any, V : Any> map(vararg data: Any): Map<K, V> = fill(mutableMapOf(), *data)

    fun <K : Any, V : Any> mapFrom(source: () -> MutableMap<K, V>, vararg data: Any): Map<K, V> = fill(source(), *data)

    @Suppress("UNCHECKED_CAST")
    private fun <K : Any, V : Any> fill(map: MutableMap<K, V>, vararg data: Any): Map<K, V> {
        var i = 0
        while (i < data.size) {
            map[data[i] as K] = data[i + 1] as V
            i += 2
        }
        return map
    }

    fun <K : Any, V : Any> multiMap(keyType: KClass<K>, valueType: KClass<V>, vararg data: Any): Map<K, MutableList<V>> =
        multiMapFrom(keyType, valueType, { mutableMapOf() }, { mutableListOf() }, *data)

    @Suppress("UNCHECKED_CAST")
    private fun <K : Any, V : Any> multiMapFrom(
        keyType: KClass<K>,
        valueType: KClass<V>,
        mapSource: () -> MutableMap<K, MutableList<V>>,
        listSource: () -> MutableList<V>,
        vararg data: Any
    ): Map<K, MutableList<V>> {
        val map = mapSource()
        lateinit var key: K
        for (el in data) {
            if (keyType.isInstance(el)) {
                key = el as K
            } else if (valueType.isInstance(el)) {
                vs(map, key, listSource).add(el as V)
            } else {
                throw IllegalArgumentException()
            }
        }
        return map
    }

    private fun <K : Any, V : Any> vs(
        map: MutableMap<K, MutableList<V>>, key: K, listSource: () -> MutableList<V>
    ): MutableList<V> {
        return if (map[key] == null) {
            val newValue = listSource()
            map[key] = newValue
            newValue
        } else map[key]!!
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

    fun <K : Any, V : Any> multiMapAdd(map: MutableMap<K, MutableList<V>>, key: K, value: V): Map<K, MutableList<V>> =
        multiMapAdd({ mutableListOf() }, map, key, value)

    private fun <K : Any, V : Any> multiMapAdd(
        listSource: () -> MutableList<V>, map: MutableMap<K, MutableList<V>>, key: K, value: V
    ): Map<K, MutableList<V>> {
        vs(map, key) { listSource() }.add(value)
        return map
    }

    fun <K : Any, V : Any> multiMapAddAll(
        map: MutableMap<K, MutableList<V>>, key: K, vararg values: V
    ): Map<K, MutableList<V>> = multiMapAddAll({ mutableListOf() }, map, key, *values)

    private fun <K : Any, V : Any> multiMapAddAll(
        listSource: () -> MutableList<V>, map: MutableMap<K, MutableList<V>>, key: K, vararg values: V
    ): Map<K, MutableList<V>> {
        vs(map, key) { listSource() }.addAll(listOf(*values))
        return map
    }

    fun <K : Any, V : Any> multiMapset(
        keyType: KClass<K>, valueType: KClass<V>, vararg data: Any
    ): Map<K, MutableSet<V>> = multiMapsetFrom(keyType, valueType, { mutableMapOf() }, { mutableSetOf() }, *data)

    @Suppress("UNCHECKED_CAST")
    private fun <K : Any, V : Any> multiMapsetFrom(
        keyType: KClass<K>,
        valueType: KClass<V>,
        mapSource: () -> MutableMap<K, MutableSet<V>>,
        listSource: () -> MutableSet<V>,
        vararg data: Any
    ): Map<K, MutableSet<V>> {
        val map = mapSource()
        lateinit var key: K
        for (el in data) {
            if (keyType.isInstance(el)) {
                key = el as K
            } else if (valueType.isInstance(el)) {
                vs(map, key) { listSource() }.add(el as V)
            } else {
                throw IllegalArgumentException()
            }
        }
        return map
    }

    fun <K : Any, V : Any> multiMapsetAdd(map: MutableMap<K, MutableSet<V>>, key: K, value: V): Map<K, MutableSet<V>> =
        multiMapsetAdd({ mutableSetOf() }, map, key, value)

    private fun <K : Any, V : Any> multiMapsetAdd(
        listSource: () -> MutableSet<V>, map: MutableMap<K, MutableSet<V>>, key: K, value: V
    ): Map<K, MutableSet<V>> {
        vs(map, key) { listSource() }.add(value)
        return map
    }

    fun <K : Any, V : Any> multiMapsetAddAll(
        map: MutableMap<K, MutableSet<V>>, key: K, vararg values: V
    ): Map<K, MutableSet<V>> = multiMapsetAddAll({ mutableSetOf() }, map, key, *values)

    private fun <K : Any, V : Any> multiMapsetAddAll(
        listSource: () -> MutableSet<V>, map: MutableMap<K, MutableSet<V>>, key: K, vararg values: V
    ): Map<K, MutableSet<V>> {
        vs(map, key) { listSource() }.addAll(listOf(*values))
        return map
    }

    fun <S, T> transform(source: List<S>, mapper: (S) -> T): List<T> = source.map(mapper)
}
