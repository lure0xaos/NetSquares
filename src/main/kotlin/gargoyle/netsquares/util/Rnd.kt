package gargoyle.netsquares.util

import java.util.Random
import kotlin.reflect.KClass

object Rnd {
    private val r = Random()
    fun rnd(a: Int, b: Int): Int {
        return r.nextInt(b - a + 1) + a
    }

    fun <T> rnd(enumClass: KClass<T>): T
            where T : Enum<T> {
        return rnd(enumClass.java.enumConstants)
    }

    private fun <T : Any> rnd(array: Array<T>): T {
        return array[r.nextInt(array.size)]
    }
}
