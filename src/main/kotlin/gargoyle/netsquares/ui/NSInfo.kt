package gargoyle.netsquares.ui

import gargoyle.netsquares.model.Game
import gargoyle.netsquares.res.Messages
import gargoyle.netsquares.ui.painter.NSPainter
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D
import javax.swing.JPanel

class NSInfo(private val messages: Messages, var painter: NSPainter, private val game: Game) : JPanel() {

    init {
        isOpaque = false
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        painter.paintInfo(
            g as Graphics2D,
            game,
            Rectangle2D.Double(0.0, 0.0, width.toDouble(), height.toDouble()),
            messages
        )
    }
}
