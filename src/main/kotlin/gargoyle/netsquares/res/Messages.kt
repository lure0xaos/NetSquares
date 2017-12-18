package gargoyle.netsquares.res

import gargoyle.netsquares.util.log.LoggerFactory
import java.util.*

class Messages internal constructor(context: NSContext, name: String) {
    private val logger = LoggerFactory.getLogger(Messages::class)

    private lateinit var bundle: ResourceBundle

    init {
        try {
            bundle = context.loadBundle(name)
        } catch (e: MissingResourceException) {
            logger.error("$name not found")
        }
    }

    fun format(key: String, vararg args: Any): String {
        return try {
            val s = if (::bundle.isInitialized && bundle.containsKey(key)) bundle.getString(key) else key
            String.format(s, *args)
        } catch (e: MissingResourceException) {
            logger.error("${e.className}.$key not found")
            key
        }
    }

    operator fun get(key: String): String {
        return try {
            if (::bundle.isInitialized && bundle.containsKey(key)) bundle.getString(key) else key
        } catch (e: MissingResourceException) {
            logger.error("${e.className}.$key not found")
            key
        }
    }
}
