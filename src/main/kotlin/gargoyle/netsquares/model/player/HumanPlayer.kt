package gargoyle.netsquares.model.player

import gargoyle.netsquares.model.*
import gargoyle.netsquares.ui.listener.NSKeyListener
import gargoyle.netsquares.ui.listener.NSMouseListener
import java.util.*

class HumanPlayer(name: String) : Player(name), NSKeyListener, NSMouseListener {
    val keyMap: KeyMap = KeyMap()
    private val persistentActions: MutableCollection<Action> = LinkedHashSet()
    private val stackedActions: Queue<Action> = ArrayDeque()
    override fun move(board: Board, directions: Directions): Move? {
        if (stackedActions.isNotEmpty()) {
            return stackedActions.poll().move
        } else {
            for (action in persistentActions) {
                val move = action.move
                if (move.isAllowed(directions)) {
                    if (move == Move.OPEN) {
                        persistentActions.clear()
                    }
                    return move
                }
            }
            return null
        }
    }

    override fun onClick(actions: Array<Action>) {
        stackedActions += actions
    }

    override fun onKeyPress(action: Action) {
        persistentActions += action
    }

    override fun onKeyRelease(action: Action) {
        persistentActions.remove(action)
    }
}
