package gargoyle.netsquares.ui.painter.state

import java.awt.Graphics2D
import java.util.Stack

class GraphicsStateManager(private val graphics: Graphics2D, private val autoRestore: Boolean) : AutoCloseable {
    private val states = Stack<GraphicsState>()

    init {
        if (autoRestore) {
            save()
        }
    }

    private fun checkEmpty() {
        require(states.empty()) { "" }
    }

    override fun close() {
        if (autoRestore) {
            while (!isEmpty) {
                restore()
            }
        }
        checkEmpty()
        reset()
    }

    private val isEmpty: Boolean
        get() = states.empty()

    fun restore() {
        val state = states.pop()
        state?.restore(graphics)
    }

    val state: GraphicsState
        get() = states.peek()

    private fun reset() {
        states.clear()
    }

    fun save() {
        states.push(GraphicsState(graphics))
    }

    override fun toString(): String {
        return "GraphicsStateManager{" +
                "autoRestore=" + autoRestore +
                ", top state=" + states.peek() +
                ", size=" + states.size +
                ", states=" + states +
                '}'
    }
}
