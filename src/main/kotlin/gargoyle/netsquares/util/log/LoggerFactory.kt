package gargoyle.netsquares.util.log

import kotlin.reflect.KClass

object LoggerFactory {
    fun getLogger(aClass: KClass<*>): Logger = JULLogger(aClass.qualifiedName ?: "")

    fun getLogger(name: String): Logger = JULLogger(name)
}
