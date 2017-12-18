package gargoyle.netsquares.ui.painter

import gargoyle.netsquares.model.*
import gargoyle.netsquares.res.Messages
import gargoyle.netsquares.res.NSContext
import gargoyle.netsquares.ui.painter.state.GraphicsStateManager
import gargoyle.netsquares.util.InterpolUtils
import gargoyle.netsquares.util.SwingDraw
import java.awt.*
import java.awt.geom.Rectangle2D
import kotlin.math.abs

class NSPainter(context: NSContext, background: String, backgroundSuffix: String) {
    private val background: Image = context.loadImage(background, backgroundSuffix)
    private val fontLCD: Font = context.loadFont(RES_FONT_LCD)
    private val fontZX: Font = context.loadFont(RES_FONT_ZX)

    fun getCellAt(bounds: Rectangle2D, boardSize: Int, x: Double, y: Double): Point =
        Point(
            ((x - bounds.x) * boardSize / bounds.width).toInt(),
            ((y - bounds.y) * boardSize / bounds.height).toInt()
        )

    fun paintBoard(g: Graphics2D, game: Game, bounds: Rectangle2D) {
        GraphicsStateManager(g, true).use { manager ->
            when {
                game.isState(GameState.GAME) -> {
                    manager.save()
                    paintBoardInGame(
                        manager, g, bounds, game.board,
                        game.getAllowed(requireNotNull(game.currentPlayer))
                    )
                    manager.restore()
                }

                game.isState(GameState.OPEN) -> {
                    manager.save()
                    paintBoardInOpen(manager, g, game, bounds)
                    manager.restore()
                }
            }
        }
    }

    private fun paintBoardInGame(
        manager: GraphicsStateManager,
        g: Graphics2D,
        bounds: Rectangle2D,
        board: Board,
        allowed: Directions
    ) {
        g.color = CELL_BORDER
        g.fill(bounds)
        paintBackground(g, background, bounds)
        board.each { x: Int, y: Int ->
            manager.save()
            val cellBounds = getCellBounds(bounds, board.size, x, y)
            g.clip = cellBounds
            val reachable = allowed.isReachable(board.caretX, board.caretY, x, y)
            paintCellInGame(g, board.isOpenAt(x, y), board.getAt(x, y), board.isCaretAt(x, y), reachable, cellBounds)
            manager.restore()
            null
        }
    }

    private fun paintBackground(g: Graphics2D, background: Image, bounds: Rectangle2D) {
        val inner: Rectangle2D =
            Rectangle2D.Double(
                0.0, 0.0,
                background.getWidth(null).toDouble(), background.getHeight(null).toDouble()
            )
        val fit = Rectangle2D.Double()
        if (bounds.width > bounds.height) {
            fit.height = bounds.height
            fit.width = inner.height / inner.width * bounds.height
        } else {
            fit.width = bounds.width
            fit.height = inner.height / inner.width * bounds.width
        }
        fit.x = bounds.x + (bounds.width - fit.getWidth()) / 2
        fit.y = bounds.y + (bounds.height - fit.getHeight()) / 2
        g.drawImage(
            background,
            fit.getX().toInt(),
            fit.getY().toInt(),
            (fit.getX() + fit.getWidth()).toInt(),
            (fit.getY() + fit.getHeight()).toInt(),
            0,
            0,
            background.getWidth(null),
            background.getHeight(null),
            null
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun paintBoardInOpen(manager: GraphicsStateManager, g: Graphics2D, game: Game, bounds: Rectangle2D) {
        g.color = CELL_BORDER
        g.fill(bounds)
        paintBackground(g, background, bounds)
    }

    private fun getCellBounds(bounds: Rectangle2D, boardSize: Int, x: Int, y: Int): Rectangle2D.Double {
        val cellWidth = bounds.width / boardSize
        val cellHeight = bounds.height / boardSize
        return Rectangle2D.Double(bounds.x + x * cellWidth, bounds.y + y * cellHeight, cellWidth, cellHeight)
    }

    private fun paintCellInGame(
        g: Graphics2D,
        open: Boolean,
        score: Int,
        caret: Boolean,
        reachable: Boolean,
        bounds: Rectangle2D
    ) {
        if (open) {
            if (caret) {
                paintCellCaretInGame(g, bounds)
            }
        } else {
            val cellBackground =
                if (score > 0)
                    if (reachable)
                        CELL_POSITIVE_HIGHLIGHT
                    else
                        CELL_POSITIVE
                else if (reachable)
                    CELL_NEGATIVE_HIGHLIGHT
                else
                    CELL_NEGATIVE
            g.color = cellBackground
            g.fill(bounds)
            paintCellBorderInGame(g, bounds, cellBackground)
            g.color = CELL_BORDER
            g.draw(bounds)
            if (caret) {
                paintCellCaretInGame(g, bounds)
            }
            g.color = CELL_LABEL
            g.font = fontZX
            val label = abs(score).toString()
            SwingDraw.updateFontSize(g, bounds, label)
            SwingDraw.drawStringCentered(g, bounds, label)
        }
    }

    private fun paintCellCaretInGame(g: Graphics2D, bounds: Rectangle2D) {
        paintCellBorderInGame(g, bounds, CELL_CARET)
    }

    private fun paintCellBorderInGame(g: Graphics2D, bounds: Rectangle2D, color: Color) {
        val range = InterpolUtils.toRange((bounds.width / 10).toInt(), CARET_WIDTH_MIN, CARET_WIDTH_MAX)
        for (i in 0 until range) {
            g.color = InterpolUtils.interpolate(CELL_BORDER, color, range, i)
            g.draw(Rectangle2D.Double(bounds.x + i, bounds.y + i, bounds.width - i - i, bounds.height - i - i))
        }
    }

    fun paintInfo(g: Graphics2D, game: Game, bounds: Rectangle2D, messages: Messages) {
        GraphicsStateManager(g, true).use { manager ->
            manager.save()
            val state = game.state
            if (game.isState(GameState.GAME, GameState.OPEN)) {
                paintInfoInGame(manager, g, bounds, messages, state, game.getPlayers(), game.currentPlayer)
            }
            manager.restore()
        }
    }

    private fun paintInfoInGame(
        manager: GraphicsStateManager,
        g: Graphics2D,
        bounds: Rectangle2D,
        messages: Messages,
        state: GameState,
        players: List<Player>,
        currentPlayer: Player?
    ) {
        val barWidth = bounds.width
        val barHeight = bounds.height / players.size
        for (p in players.indices) {
            manager.save()
            val player = players[p]
            val current = player == currentPlayer
            val barBounds = Rectangle2D.Double(bounds.x, bounds.y + barHeight * p, barWidth, barHeight)
            g.clip = barBounds
            paintInfoBarInGame(manager, g, barBounds, messages, player, current, state)
            manager.restore()
        }
    }

    private fun paintInfoBarInGame(
        manager: GraphicsStateManager,
        g: Graphics2D,
        bounds: Rectangle2D,
        messages: Messages,
        player: Player,
        current: Boolean,
        state: GameState
    ) {
        g.color = INFOBAR_BG
        g.fill(bounds)
        g.color = INFOBAR_BORDER
        g.draw(bounds)
        val rows = 3
        var row = 0
        row = paintInfoBarRowInGame(manager, g, bounds, player.name, fontZX, INFOBAR_PLAYER, row, rows)
        val score = player.score
        val playerColor = if (score >= 0) INFOBAR_POSITIVE else INFOBAR_NEGATIVE
        row = paintInfoBarRowInGame(manager, g, bounds, score.toString(), fontLCD, playerColor, row, rows)
        if (state.isInState(GameState.GAME)) {
            val statusColor = if (current) INFOBAR_PLAYER_MOVE else INFOBAR_PLAYER_WAIT
            val statusLabel = if (current) messages[MSG_INFOBAR_STATUS_MOVE] else messages[MSG_INFOBAR_STATUS_WAIT]
            row = paintInfoBarRowInGame(manager, g, bounds, statusLabel, fontZX, statusColor, row, rows)
        }
        assert(row == rows)
    }

    private fun paintInfoBarRowInGame(
        manager: GraphicsStateManager,
        g: Graphics2D,
        bounds: Rectangle2D,
        nameLabel: String,
        font: Font,
        color: Color,
        row: Int,
        rows: Int
    ): Int {
        manager.save()
        g.font = font
        g.color = color
        val barWidth = bounds.width
        val barHeight = bounds.height / rows
        val nameBounds = Rectangle2D.Double(bounds.x, bounds.y + barHeight * row, barWidth, barHeight)
        g.clip = nameBounds
        SwingDraw.updateFontSize(g, nameBounds, nameLabel)
        SwingDraw.drawStringCentered(g, nameBounds, nameLabel)
        manager.restore()
        return row + 1
    }

    companion object {
        private const val CARET_WIDTH_MAX = 10
        private const val CARET_WIDTH_MIN = 5
        private val CELL_BORDER = Color.BLACK
        private val CELL_CARET = Color.BLUE
        private val CELL_LABEL = Color.BLACK
        private val CELL_NEGATIVE = Color.RED
        private val CELL_NEGATIVE_HIGHLIGHT = InterpolUtils.interpolate(CELL_NEGATIVE, Color.WHITE, 2, 1)
        private val CELL_POSITIVE = Color.GREEN
        private val CELL_POSITIVE_HIGHLIGHT = InterpolUtils.interpolate(CELL_POSITIVE, Color.WHITE, 2, 1)
        private val INFOBAR_BG = Color.BLACK
        private val INFOBAR_BORDER = Color.BLUE
        private val INFOBAR_NEGATIVE = Color.RED
        private val INFOBAR_PLAYER = Color.WHITE
        private val INFOBAR_PLAYER_MOVE = Color.GREEN
        private val INFOBAR_PLAYER_WAIT = Color.RED
        private val INFOBAR_POSITIVE = Color.GREEN
        private const val MSG_INFOBAR_STATUS_MOVE = "infobar.status.move"
        private const val MSG_INFOBAR_STATUS_WAIT = "infobar.status.wait"
        private const val RES_FONT_LCD = "gargoyle/netsquares/res/digital-7.ttf"
        private const val RES_FONT_ZX = "gargoyle/netsquares/res/zx_spectrum-7.ttf"
    }
}
