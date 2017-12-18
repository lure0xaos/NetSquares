package gargoyle.netsquares.util.log;

import java.text.MessageFormat;

public interface Logger {
    default void debug(String message, Object[] args, Throwable exception) {
        log(Level.DEBUG, message, args, exception);
    }

    default void log(Level level, String message, Object[] args, Throwable exception) {
        log(level, MessageFormat.format(message, args), exception);
    }

    void log(Level level, String message, Throwable exception);

    default void debug(String message, Object... args) {
        log(Level.DEBUG, message, args);
    }

    default void log(Level level, String message, Object... args) {
        log(level, MessageFormat.format(message, args), (Throwable) null);
    }

    default void error(String message, Throwable exception) {
        log(Level.ERROR, message, exception);
    }

    default void debug(String message, Object arg) {
        log(Level.DEBUG, message, arg);
    }

    default void error(String message, Object[] args, Throwable exception) {
        log(Level.ERROR, message, args, exception);
    }

    default void log(Level level, String message, Object arg) {
        log(level, MessageFormat.format(message, arg), (Throwable) null);
    }

    default void error(String message, Object... args) {
        log(Level.ERROR, message, args);
    }

    default void debug(String message, Object arg1, Object arg2) {
        log(Level.DEBUG, message, arg1, arg2);
    }

    default void error(String message, Object arg) {
        log(Level.ERROR, message, arg);
    }

    default void log(Level level, String message, Object arg1, Object arg2) {
        log(level, MessageFormat.format(message, arg1, arg2), (Throwable) null);
    }

    default void error(String message, Object arg1, Object arg2) {
        log(Level.ERROR, message, arg1, arg2);
    }

    default void debug(String message) {
        log(Level.DEBUG, message);
    }

    default void error(String message) {
        log(Level.ERROR, message);
    }

    default void log(Level level, String message) {
        log(level, message, (Throwable) null);
    }

    default void info(String message, Throwable exception) {
        log(Level.INFO, message, exception);
    }

    default void info(String message, Object[] args, Throwable exception) {
        log(Level.INFO, message, args, exception);
    }

    default void info(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    default void info(String message, Object arg) {
        log(Level.INFO, message, arg);
    }

    default void info(String message, Object arg1, Object arg2) {
        log(Level.INFO, message, arg1, arg2);
    }

    default void info(String message) {
        log(Level.INFO, message);
    }

    default void trace(String message, Object[] args, Throwable exception) {
        log(Level.TRACE, message, args, exception);
    }

    default void trace(String message, Object... args) {
        log(Level.TRACE, message, args);
    }

    default void trace(String message, Object arg) {
        log(Level.TRACE, message, arg);
    }

    default void trace(String message, Object arg1, Object arg2) {
        log(Level.TRACE, message, arg1, arg2);
    }

    default void trace(String message) {
        log(Level.TRACE, message);
    }

    default void warn(String message, Throwable exception) {
        log(Level.WARN, message, exception);
    }

    default void warn(String message, Object[] args, Throwable exception) {
        log(Level.WARN, message, args, exception);
    }

    default void warn(String message, Object... args) {
        log(Level.WARN, message, args);
    }

    default void warn(String message, Object arg) {
        log(Level.WARN, message, arg);
    }

    default void warn(String message, Object arg1, Object arg2) {
        log(Level.WARN, message, arg1, arg2);
    }

    default void warn(String message) {
        log(Level.WARN, message);
    }
}
