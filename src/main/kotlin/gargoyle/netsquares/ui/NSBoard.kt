package gargoyle.netsquares.ui

import gargoyle.netsquares.model.Action
import gargoyle.netsquares.model.Game
import gargoyle.netsquares.model.GameState
import gargoyle.netsquares.model.Move
import gargoyle.netsquares.model.player.HumanPlayer
import gargoyle.netsquares.ui.painter.NSPainter
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.Rectangle2D
import javax.swing.JComponent

class NSBoard(var painter: NSPainter, val game: Game) : JComponent() {


    init {
        isOpaque = false
        isFocusable = true
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                onKeyPress(e.keyCode)
            }

            override fun keyReleased(e: KeyEvent) {
                onKeyRelease(e.keyCode)
            }
        })
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (game.isState(GameState.GAME)) {
                    onMouseClick(e.x, e.y, painter)
                }
            }
        })
    }

    private fun onKeyPress(keyCode: Int) {
        val player = game.currentPlayer
        if (player is HumanPlayer) {
            val keyMap = player.keyMap
            val action = keyMap.actionForKey(keyCode)
            if (action != null) {
                player.onKeyPress(action)
            }
        }
    }

    private fun onKeyRelease(keyCode: Int) {
        val player = game.currentPlayer
        if (player is HumanPlayer) {
            val keyMap = player.keyMap
            val action = keyMap.actionForKey(keyCode)
            if (action != null) {
                player.onKeyRelease(action)
            }
        }
    }

    private fun onMouseClick(mouseX: Int, mouseY: Int, painter: NSPainter) {
        val player = game.currentPlayer
        if (player is HumanPlayer) {
            val board = game.board
            val cellAt = painter.getCellAt(relBounds, board.size, mouseX.toDouble(), mouseY.toDouble())
            val caretX = board.caretX
            val caretY = board.caretY
            val x = cellAt.x
            val y = cellAt.y
            if (x == caretX && y == caretY) {
                player.onClick(arrayOf(Action.FIRE))
            } else {
                val directions = game.getAllowed(player)
                if (directions.isReachable(caretX, caretY, x, y)) {
                    val reachable = directions.getReachable(caretX, caretY, x, y)
                    val length = reachable?.size ?: 0
                    val actions = arrayOfNulls<Action>(length + 1)
                    for (i in 0 until length) {
                        actions[i] = Action.forMove(Move.forDirection(reachable!![i]))
                    }
                    actions[length] = Action.FIRE
                    player.onClick(actions.map { it!! }.toTypedArray())
                }
            }
        }
    }

    private val relBounds: Rectangle2D.Double
        get() = Rectangle2D.Double(0.0, 0.0, width.toDouble(), height.toDouble())

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        painter.paintBoard(g as Graphics2D, game, relBounds)
    }
}
