package gargoyle.netsquares.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.Callable;


public final class Assert {
    private Assert() {
    }

    public static void assertEmpty(@Nullable String value, @NotNull String message, Object... args) {
        check(() -> value == null || value.trim().isEmpty(), message, args);
    }

    public static <T> void assertEquals(@Nullable T actual, @Nullable T expected, @NotNull String message, Object... args) {
        check(() -> Objects.equals(actual, expected), message, args);
    }

    public static <T extends Comparable<T>> void assertInRange(@NotNull T actual, @Nullable T min, @Nullable T max, @NotNull String message, Object... args) {
        check(() -> (min == null || min.compareTo(actual) <= 0) && (max == null || max.compareTo(actual) >= 0), message, args);
    }

    public static void assertNotEmpty(@Nullable String value, @NotNull String message, Object... args) {
        check(() -> value != null && !value.trim().isEmpty(), message, args);
    }

    public static <T> void assertNotEquals(@Nullable T actual, @Nullable T expected, @NotNull String message, Object... args) {
        check(() -> !Objects.equals(actual, expected), message, args);
    }

    public static <T extends Comparable<T>> void assertNotInRange(@NotNull T actual, @Nullable T min, @Nullable T max, @NotNull String message, Object... args) {
        check(() -> min != null && min.compareTo(actual) > 0 && (max == null || max.compareTo(actual) >= 0), message, args);
    }

    @Contract("null,_,_->fail")
    public static <T> void assertNotNull(@Nullable T value, @NotNull String message, Object... args) {
        check(() -> value != null, message, args);
    }

    public static <T> void assertNotSame(@Nullable T actual, @Nullable T expected, @NotNull String message, Object... args) {
        check(() -> actual != expected, message, args);
    }

    public static <T> void assertSame(@Nullable T actual, @Nullable T expected, @NotNull String message, Object... args) {
        check(() -> actual == expected, message, args);
    }

    public static void assertTrue(boolean value, @NotNull String message, Object... args) {
        check(() -> value, message, args);
    }

    private static void check(@NotNull Callable<Boolean> operation, @NotNull String message, Object... args) {
        try {
            if (operation.call()) {
                return;
            }
        } catch (Exception e) {
            //
        }
        throw new IllegalArgumentException(MessageFormat.format(message, args));
    }
}
