package gargoyle.netsquares.model

enum class Direction(val deltaX: Int, val deltaY: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    fun getReachable(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        val dx = x2 - x1
        val dy = y2 - y1
        return if (sgn(dx) == deltaX && sgn(dy) == deltaY) {
            (if (deltaX == 0) 0 else dx / deltaX) + if (deltaY == 0) 0 else dy / deltaY
        } else -1
    }

    fun isAllowed(allowed: Directions): Boolean {
        return allowed.isAllowed(this)
    }

    val isHorizontal: Boolean
        get() = deltaY == 0

    fun isReachable(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return sgn(x2 - x1) == deltaX && sgn(y2 - y1) == deltaY
    }

    val isVertical: Boolean
        get() = deltaX == 0

    override fun toString(): String {
        return String.format("Direction(%s):%d,%d", name, deltaX, deltaY)
    }

    companion object {
        private fun sgn(v: Int): Int {
            return if (v == 0) 0 else if (v < 0) -1 else 1
        }
    }
}
