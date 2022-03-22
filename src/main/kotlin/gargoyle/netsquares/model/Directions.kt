package gargoyle.netsquares.model

enum class Directions(vararg directions: Direction) {
    VERTICALS(Direction.UP, Direction.DOWN), HORIZONTALS(Direction.LEFT, Direction.RIGHT);

    private val directions: Array<Direction>

    fun getReachable(x1: Int, y1: Int, x2: Int, y2: Int): Array<Direction>? {
        for (direction in getDirections()) {
            if (direction.isReachable(x1, y1, x2, y2)) {
                return Array(direction.getReachable(x1, y1, x2, y2)) { direction }
            }
        }
        return null
    }

    fun getDirections(): Array<Direction> = directions.copyOf()

    fun isAllowed(direction: Direction?): Boolean = (directions).any { dir: Direction -> direction == dir }

    fun isReachable(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        for (direction in getDirections()) {
            if (direction.isReachable(x1, y1, x2, y2)) {
                return true
            }
        }
        return false
    }

    override fun toString(): String = "Directions{directions=${directions.contentToString()}}"

    init {
        this.directions = arrayOf(*(directions)).copyOf()
    }
}
