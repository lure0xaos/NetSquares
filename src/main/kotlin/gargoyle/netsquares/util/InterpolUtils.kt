package gargoyle.netsquares.util

import java.awt.Color

object InterpolUtils {
    fun interpolate(color1: Color, color2: Color, steps: Int, step: Int): Color {
        val red = interpolate(color1.red, color2.red, steps, step)
        val green = interpolate(color1.green, color2.green, steps, step)
        val blue = interpolate(color1.blue, color2.blue, steps, step)
        return Color(red, green, blue)
    }

    private fun interpolate(value1: Int, value2: Int, steps: Int, step: Int): Int {
        return (value1 + (value2 - value1) / steps.toDouble() * step).toInt()
    }

    fun toRange(v: Int, min: Int, max: Int): Int {
        return min.coerceAtLeast(v.coerceAtMost(max))
    }
}
