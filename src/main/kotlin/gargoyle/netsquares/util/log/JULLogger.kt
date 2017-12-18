package gargoyle.netsquares.util.log

internal class JULLogger(name: String) : Logger {
    private val logger: java.util.logging.Logger = java.util.logging.Logger.getLogger(name)

    override fun log(level: Level, message: String, exception: Throwable?) =
        logger.log(toLevel(level), message, exception)

    private fun toLevel(level: Level): java.util.logging.Level =
        when (level) {
            Level.ERROR -> java.util.logging.Level.SEVERE
            Level.WARN -> java.util.logging.Level.WARNING
            Level.INFO -> java.util.logging.Level.INFO
            Level.DEBUG -> java.util.logging.Level.FINE
            Level.TRACE -> java.util.logging.Level.FINER
        }
}
