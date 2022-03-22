package gargoyle.netsquares.util.log

import java.text.MessageFormat

interface Logger {
    fun debug(message: String, args: Array<Any>, exception: Throwable?) {
        log(Level.DEBUG, message, args, exception)
    }

    fun log(level: Level, message: String, args: Array<Any>, exception: Throwable?) {
        log(level, MessageFormat.format(message, *args), exception)
    }

    fun log(level: Level, message: String, exception: Throwable?)
    fun debug(message: String, vararg args: Any) {
        log(Level.DEBUG, message, *args)
    }

    fun log(level: Level, message: String, vararg args: Any) {
        log(level, MessageFormat.format(message, *args), null as Throwable?)
    }

    fun error(message: String, exception: Throwable?) {
        log(Level.ERROR, message, exception)
    }

    fun debug(message: String, arg: Any) {
        log(Level.DEBUG, message, arg)
    }

    fun error(message: String, args: Array<Any>, exception: Throwable?) {
        log(Level.ERROR, message, args, exception)
    }

    fun log(level: Level, message: String, arg: Any) {
        log(level, MessageFormat.format(message, arg), null as Throwable?)
    }

    fun error(message: String, vararg args: Any) {
        log(Level.ERROR, message, *args)
    }

    fun debug(message: String, arg1: Any, arg2: Any) {
        log(Level.DEBUG, message, arg1, arg2)
    }

    fun error(message: String, arg: Any) {
        log(Level.ERROR, message, arg)
    }

    fun log(level: Level, message: String, arg1: Any, arg2: Any) {
        log(level, MessageFormat.format(message, arg1, arg2), null as Throwable?)
    }

    fun error(message: String, arg1: Any, arg2: Any) {
        log(Level.ERROR, message, arg1, arg2)
    }

    fun debug(message: String) {
        log(Level.DEBUG, message)
    }

    fun error(message: String) {
        log(Level.ERROR, message)
    }

    fun log(level: Level, message: String) {
        log(level, message, null as Throwable?)
    }

    fun info(message: String, exception: Throwable?) {
        log(Level.INFO, message, exception)
    }

    fun info(message: String, args: Array<Any>, exception: Throwable?) {
        log(Level.INFO, message, args, exception)
    }

    fun info(message: String, vararg args: Any) {
        log(Level.INFO, message, *args)
    }

    fun info(message: String, arg: Any) {
        log(Level.INFO, message, arg)
    }

    fun info(message: String, arg1: Any, arg2: Any) {
        log(Level.INFO, message, arg1, arg2)
    }

    fun info(message: String) {
        log(Level.INFO, message)
    }

    fun trace(message: String, args: Array<Any>, exception: Throwable?) {
        log(Level.TRACE, message, args, exception)
    }

    fun trace(message: String, vararg args: Any) {
        log(Level.TRACE, message, *args)
    }

    fun trace(message: String, arg: Any) {
        log(Level.TRACE, message, arg)
    }

    fun trace(message: String, arg1: Any, arg2: Any) {
        log(Level.TRACE, message, arg1, arg2)
    }

    fun trace(message: String) {
        log(Level.TRACE, message)
    }

    fun warn(message: String, exception: Throwable?) {
        log(Level.WARN, message, exception)
    }

    fun warn(message: String, args: Array<Any>, exception: Throwable?) {
        log(Level.WARN, message, args, exception)
    }

    fun warn(message: String, vararg args: Any) {
        log(Level.WARN, message, *args)
    }

    fun warn(message: String, arg: Any) {
        log(Level.WARN, message, arg)
    }

    fun warn(message: String, arg1: Any, arg2: Any) {
        log(Level.WARN, message, arg1, arg2)
    }

    fun warn(message: String) {
        log(Level.WARN, message)
    }
}
