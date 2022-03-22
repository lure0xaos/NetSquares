package gargoyle.netsquares.util

object Assert {
    fun assertEmpty(value: String?, message: () -> String) {
        require(value == null || value.trim().isEmpty(), message)
    }

    fun <T : Any> assertEquals(actual: T, expected: T, message: () -> String) {
        require(actual == expected, message)
    }

    fun <T : Comparable<T>> assertInRange(actual: T, min: T, max: T, message: () -> String) {
        require((min <= actual) && (max >= actual), message)
    }

    fun assertNotEmpty(value: String?, message: () -> String) {
        require(value != null && value.trim().isNotEmpty(), message)
    }

    fun <T : Any> assertNotEquals(actual: T, expected: T, message: () -> String) {
        require(actual != expected, message)
    }

    fun <T : Comparable<T>> assertNotInRange(actual: T, min: T, max: T, message: () -> String) {
        require(min > actual && (max >= actual), message)
    }

    fun <T : Any> assertNotSame(actual: T, expected: T, message: () -> String) {
        require(actual !== expected, message)
    }

    fun <T : Any> assertSame(actual: T, expected: T, message: () -> String) {
        require(actual === expected, message)
    }

    fun assertTrue(value: Boolean, message: () -> String) {
        require(value, message)
    }
}
