package gargoyle.netsquares.model

abstract class Player protected constructor(val name: String) {
    var score = 0
        private set

    init {
        reset()
    }

    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            other == null || javaClass != other.javaClass -> false
            else -> name == (other as Player).name
        }

    fun addScore(score: Int) {
        this.score += score
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    abstract fun move(board: Board, directions: Directions?): Move?

    override fun toString(): String = String.format("${javaClass.simpleName} \"$name\" ($score)")

    fun reset() {
        score = 0
    }
}
