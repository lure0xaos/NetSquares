package gargoyle.netsquares.res

import java.lang.ref.Reference
import java.lang.ref.SoftReference
import kotlin.reflect.KClass

class HybridCache<V : Any>(loaders: Map<KClass<out V>, Loader<out V>>?) {
    private val cache: MutableMap<String, Reference<V?>> = mutableMapOf()
    private val loaders: MutableMap<KClass<out V>, Loader<out V>> = mutableMapOf()

    init {
        this.loaders.putAll(loaders!!)
    }

    operator fun <T : V> get(context: NSContext, name: String, type: KClass<T>, suffix: String): T {
        val loader = getLoader(type)
        return get(context, name, loader, suffix)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : V> getLoader(type: KClass<T>): Loader<T> {
        return loaders[type]!! as Loader<T>
    }

    @Suppress("UNCHECKED_CAST")
    private operator fun <T : V> get(context: NSContext, name: String, loader: Loader<T>, suffix: String): T {
        val reference = cache[name]
        if (reference != null) {
            val value = reference.get()
            if (value != null) {
                return value as T
            }
        }
        return try {
            val value = loader.load(context, name, suffix)
            cache[name] = SoftReference(value)
            value
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }
}
