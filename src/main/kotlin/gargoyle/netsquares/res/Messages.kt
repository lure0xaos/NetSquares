package gargoyle.netsquares.res

import gargoyle.netsquares.util.log.LoggerFactory
import java.util.MissingResourceException
import java.util.ResourceBundle

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
            val s = if(!::bundle.isInitialized) key else bundle.getString(key)
            String.format(s, *args)
        } catch (e: MissingResourceException) {
            logger.error("${if(!::bundle.isInitialized) null else bundle.baseBundleName}.${key} not found")
            key
        }
    }

    operator fun get(key: String): String {
        return try {
            if(!::bundle.isInitialized) key else bundle.getString(key)
        } catch (e: MissingResourceException) {
            logger.error("${bundle.baseBundleName}.${key} not found")
            key
        }
    }
}
