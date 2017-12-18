package gargoyle.netsquares.util

import java.util.*

object Rnd {
    private val r = Random()
    fun rnd(a: Int, b: Int): Int =
        r.nextInt(b - a + 1) + a
}
