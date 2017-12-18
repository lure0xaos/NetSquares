package gargoyle.netsquares.model.player

import gargoyle.netsquares.model.Board
import gargoyle.netsquares.model.Directions
import gargoyle.netsquares.model.Move
import gargoyle.netsquares.model.Player

class DumbPlayer(name: String) : Player(name) {
    override fun move(board: Board, directions: Directions): Move? {
        val canOpen = board.canOpen()
        val openScore = board.atCaret
        var max = if (canOpen) openScore else Int.MIN_VALUE
        var move = if (canOpen) Move.OPEN else null
        for (direction in directions.getDirections()) {
            var i = 1
            while (board.canMoveBy(direction, i)) {
                if (board.canOpenBy(direction, i)) {
                    val score = board.getBy(direction, i)
                    if (max == score && move == null || max < score) {
                        max = score
                        move = Move.forDirection(direction)
                    }
                }
                i++
            }
        }
        return if (canOpen && max == openScore) Move.OPEN else move
    }
}
