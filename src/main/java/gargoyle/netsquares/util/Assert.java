package gargoyle.netsquares.util;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.Callable;

public final class Assert {
    private Assert() {
    }

    public static void assertEmpty(String value, String message, Object... args) {
        check(() -> value == null || value.trim().isEmpty(), message, args);
    }

    private static void check(Callable<Boolean> operation, String message, Object... args) {
        try {
            if (operation.call()) {
                return;
            }
        } catch (Exception e) {
            //
        }
        throw new IllegalArgumentException(MessageFormat.format(message, args));
    }

    public static <T> void assertEquals(T actual, T expected, String message, Object... args) {
        check(() -> Objects.equals(actual, expected), message, args);
    }

    public static <T extends Comparable<T>> void assertInRange(T actual, T min, T max, String message, Object... args) {
        check(() -> (min == null || min.compareTo(actual) <= 0) && (max == null || max.compareTo(actual) >= 0), message, args);
    }

    public static void assertNotEmpty(String value, String message, Object... args) {
        check(() -> value != null && !value.trim().isEmpty(), message, args);
    }

    public static <T> void assertNotEquals(T actual, T expected, String message, Object... args) {
        check(() -> !Objects.equals(actual, expected), message, args);
    }

    public static <T extends Comparable<T>> void assertNotInRange(T actual, T min, T max, String message, Object... args) {
        check(() -> min != null && min.compareTo(actual) > 0 && (max == null || max.compareTo(actual) >= 0), message, args);
    }

    public static <T> void assertNotNull(T value, String message, Object... args) {
        check(() -> value != null, message, args);
    }

    public static <T> void assertNotSame(T actual, T expected, String message, Object... args) {
        check(() -> actual != expected, message, args);
    }

    public static <T> void assertSame(T actual, T expected, String message, Object... args) {
        check(() -> actual == expected, message, args);
    }

    public static void assertTrue(boolean value, String message, Object... args) {
        check(() -> value, message, args);
    }
}
