package gargoyle.netsquares.util.log

import java.text.MessageFormat

@Suppress("unused")
interface Logger {
    fun debug(message: String, args: Array<Any>, exception: Throwable?): Unit =
        log(Level.DEBUG, message, args, exception)

    fun log(level: Level, message: String, args: Array<Any>, exception: Throwable?): Unit =
        log(level, MessageFormat.format(message, *args), exception)

    fun log(level: Level, message: String, exception: Throwable?)
    fun debug(message: String, vararg args: Any): Unit = log(Level.DEBUG, message, *args)

    fun log(level: Level, message: String, vararg args: Any): Unit =
        log(level, MessageFormat.format(message, *args), null as Throwable?)

    fun error(message: String, exception: Throwable?): Unit = log(Level.ERROR, message, exception)

    fun debug(message: String, arg: Any): Unit = log(Level.DEBUG, message, arg)

    fun error(message: String, args: Array<Any>, exception: Throwable?): Unit =
        log(Level.ERROR, message, args, exception)

    fun log(level: Level, message: String, arg: Any): Unit =
        log(level, MessageFormat.format(message, arg), null as Throwable?)

    fun error(message: String, vararg args: Any): Unit = log(Level.ERROR, message, *args)

    fun debug(message: String, arg1: Any, arg2: Any): Unit = log(Level.DEBUG, message, arg1, arg2)

    fun error(message: String, arg: Any): Unit = log(Level.ERROR, message, arg)

    fun log(level: Level, message: String, arg1: Any, arg2: Any): Unit =
        log(level, MessageFormat.format(message, arg1, arg2), null as Throwable?)

    fun error(message: String, arg1: Any, arg2: Any): Unit = log(Level.ERROR, message, arg1, arg2)

    fun debug(message: String): Unit = log(Level.DEBUG, message)

    fun error(message: String): Unit = log(Level.ERROR, message)

    fun log(level: Level, message: String): Unit = log(level, message, null as Throwable?)

    fun info(message: String, exception: Throwable?): Unit = log(Level.INFO, message, exception)

    fun info(message: String, args: Array<Any>, exception: Throwable?): Unit = log(Level.INFO, message, args, exception)

    fun info(message: String, vararg args: Any): Unit = log(Level.INFO, message, *args)

    fun info(message: String, arg: Any): Unit = log(Level.INFO, message, arg)

    fun info(message: String, arg1: Any, arg2: Any): Unit = log(Level.INFO, message, arg1, arg2)

    fun info(message: String): Unit = log(Level.INFO, message)

    fun trace(message: String, args: Array<Any>, exception: Throwable?): Unit =
        log(Level.TRACE, message, args, exception)

    fun trace(message: String, vararg args: Any): Unit = log(Level.TRACE, message, *args)

    fun trace(message: String, arg: Any): Unit = log(Level.TRACE, message, arg)

    fun trace(message: String, arg1: Any, arg2: Any): Unit = log(Level.TRACE, message, arg1, arg2)

    fun trace(message: String): Unit = log(Level.TRACE, message)

    fun warn(message: String, exception: Throwable?): Unit = log(Level.WARN, message, exception)

    fun warn(message: String, args: Array<Any>, exception: Throwable?): Unit = log(Level.WARN, message, args, exception)

    fun warn(message: String, vararg args: Any): Unit = log(Level.WARN, message, *args)

    fun warn(message: String, arg: Any): Unit = log(Level.WARN, message, arg)

    fun warn(message: String, arg1: Any, arg2: Any): Unit = log(Level.WARN, message, arg1, arg2)

    fun warn(message: String): Unit = log(Level.WARN, message)
}
