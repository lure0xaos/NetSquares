package gargoyle.netsquares.util

import java.awt.Color

object InterpolUtils {
    fun interpolate(color1: Color, color2: Color, steps: Int, step: Int): Color =
        Color(
            interpolate(color1.red, color2.red, steps, step),
            interpolate(color1.green, color2.green, steps, step),
            interpolate(color1.blue, color2.blue, steps, step)
        )

    private fun interpolate(value1: Int, value2: Int, steps: Int, step: Int): Int =
        (value1 + (value2 - value1) / steps.toDouble() * step).toInt()

    fun toRange(v: Int, min: Int, max: Int): Int =
        min.coerceAtLeast(v.coerceAtMost(max))
}
