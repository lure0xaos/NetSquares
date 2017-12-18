package gargoyle.netsquares.model

enum class Directions(vararg directions: Direction) {
    VERTICALS(Direction.UP, Direction.DOWN), HORIZONTALS(Direction.LEFT, Direction.RIGHT);

    private val directions: Array<Direction> = arrayOf(*(directions)).copyOf()

    fun getReachable(x1: Int, y1: Int, x2: Int, y2: Int): Array<Direction>? =
        getDirections().firstOrNull { it.isReachable(x1, y1, x2, y2) }?.let { directions(it, x1, y1, x2, y2) }

    private fun directions(direction: Direction, x1: Int, y1: Int, x2: Int, y2: Int) =
        Array(direction.getReachable(x1, y1, x2, y2)) { direction }

    fun getDirections(): Array<Direction> = directions.copyOf()

    fun isAllowed(direction: Direction?): Boolean = (directions).any { dir: Direction -> direction == dir }

    fun isReachable(x1: Int, y1: Int, x2: Int, y2: Int): Boolean =
        getDirections().any { it.isReachable(x1, y1, x2, y2) }

    override fun toString(): String = "Directions{directions=${directions.contentToString()}}"

}
