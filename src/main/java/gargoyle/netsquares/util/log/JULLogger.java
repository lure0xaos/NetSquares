package gargoyle.netsquares.util.log;

final class JULLogger implements Logger {
    private final java.util.logging.Logger logger;

    public JULLogger(String name) {
        logger = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public void log(Level level, String message, Throwable exception) {
        logger.log(toLevel(level), message, exception);
    }

    private java.util.logging.Level toLevel(Level level) {
        switch (level) {
            case ERROR: {
                return java.util.logging.Level.SEVERE;
            }
            case WARN: {
                return java.util.logging.Level.WARNING;
            }
            case INFO: {
                return java.util.logging.Level.INFO;
            }
            case DEBUG: {
                return java.util.logging.Level.FINE;
            }
            case TRACE: {
                return java.util.logging.Level.FINER;
            }
        }
        throw new RuntimeException(String.valueOf(level));
    }
}
