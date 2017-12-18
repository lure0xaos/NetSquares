package gargoyle.netsquares.ui.painter.state

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Shape

class GraphicsState(g: Graphics2D) {
    private val clip: Shape
    private val color: Color
    private val font: Font
    private val id: Long = lastId

    init {
        lastId++
        clip = g.clip
        color = g.color
        font = g.font
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GraphicsState) return false
        return id == other.id
    }

    override fun toString(): String {
        return String.format("GraphicsState{id=%d, clip=%s, color=%s, font=%s}", id, clip, color, font)
    }

    fun restore(g: Graphics2D) {
        g.clip = null
        g.clip = clip
        g.color = color
        g.font = font
    }

    companion object {
        private var lastId: Long = 0
    }
}
