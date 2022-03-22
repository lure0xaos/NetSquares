package gargoyle.netsquares.ui

import gargoyle.netsquares.model.Game
import gargoyle.netsquares.res.Messages
import gargoyle.netsquares.ui.painter.NSPainter
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import javax.swing.JPanel

class NSInfo(private val messages: Messages, var painter: NSPainter) : JPanel() {
    var game: Game? = null

    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (game != null) {
            painter.paintInfo(
                g as Graphics2D,
                game!!,
                Rectangle2D.Double(0.0, 0.0, width.toDouble(), height.toDouble()),
                messages
            )
        }
    }
}
