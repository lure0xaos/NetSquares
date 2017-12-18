package gargoyle.netsquares.util

import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GraphicsEnvironment
import java.awt.geom.Rectangle2D

object SwingDraw {
    fun drawStringCentered(g: Graphics2D, bounds: Rectangle2D, label: String) {
        val fm = g.fontMetrics
        g.drawString(
            label,
            (bounds.x + (bounds.width - fm.stringWidth(label)) / 2).toInt(),
            (bounds.y + fm.ascent + (bounds.height - fm.height) / 2).toInt()
        )
    }

    val maximumWindowBounds: Rectangle2D
        get() = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds

    fun updateFontSize(g: Graphics, bounds: Rectangle2D, label: String) {
        g.font =
            g.font.deriveFont(getFitFontSize(g.font, label, bounds.width.toInt(), bounds.height.toInt(), g).toFloat())
    }

    private fun getFitFontSize(font: Font, text: String, width: Int, height: Int, g: Graphics): Int =
        (font.size * width.toDouble() / g.getFontMetrics(font).stringWidth(text).toDouble()).toInt()
            .coerceAtMost(height)
}
