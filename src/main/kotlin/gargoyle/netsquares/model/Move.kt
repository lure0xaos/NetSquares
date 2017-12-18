package gargoyle.netsquares.model

enum class Move(val direction: Direction?) {
    UP(Direction.UP), DOWN(Direction.DOWN), LEFT(Direction.LEFT), RIGHT(Direction.RIGHT), OPEN(null);

    fun isAllowed(allowed: Directions?): Boolean = this == OPEN || allowed!!.isAllowed(direction)

    override fun toString(): String = "Move.$name($direction)"

    companion object {
        fun forDirection(direction: Direction?): Move? =
            values().firstOrNull { it.direction == direction }
    }
}
