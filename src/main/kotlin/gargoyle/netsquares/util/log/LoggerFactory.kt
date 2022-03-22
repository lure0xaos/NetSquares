package gargoyle.netsquares.util.log

import kotlin.reflect.KClass

object LoggerFactory {
    fun getLogger(aClass: KClass<*>): Logger {
        return getLogger(aClass.qualifiedName!!)
    }

    fun getLogger(name: String): Logger {
        return JULLogger(name)
    }
}
