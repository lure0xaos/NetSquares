package gargoyle.netsquares.model.player

import gargoyle.netsquares.model.Action
import gargoyle.netsquares.model.Board
import gargoyle.netsquares.model.Directions
import gargoyle.netsquares.model.KeyMap
import gargoyle.netsquares.model.Move
import gargoyle.netsquares.model.Player
import gargoyle.netsquares.ui.listener.NSKeyListener
import gargoyle.netsquares.ui.listener.NSMouseListener
import java.util.ArrayDeque
import java.util.Queue

class HumanPlayer(name: String) : Player(name), NSKeyListener, NSMouseListener {
    private val persistentActions: MutableCollection<Action> = LinkedHashSet()
    private val stackedActions: Queue<Action> = ArrayDeque()
    var keyMap = KeyMap()
    override fun move(board: Board, directions: Directions?): Move? {
        return if (stackedActions.isEmpty()) {
            val iterator: Iterator<Action> = persistentActions.iterator()
            if (iterator.hasNext()) {
                val action = iterator.next()
                val move = action.move
                if (move.isAllowed(directions)) {
                    if (move == Move.OPEN) {
                        persistentActions.clear()
                    }
                    return move
                }
            }
            null
        } else {
            stackedActions.poll().move
        }
    }

    override fun onClick(actions: Array<Action?>) {
        stackedActions.addAll(listOf(*actions))
    }

    override fun onKeyPress(action: Action?) {
        if (action != null) {
            persistentActions.add(action)
        }
    }

    override fun onKeyRelease(action: Action?) {
        if (action != null) {
            persistentActions.remove(action)
        }
    }
}
