package gargoyle.netsquares.model

enum class Action(val move: Move) {
    UP(Move.UP), DOWN(Move.DOWN), LEFT(Move.LEFT), RIGHT(Move.RIGHT), FIRE(Move.OPEN);

    companion object {
        fun forMove(move: Move?): Action? {
            for (action in values()) {
                if (action.move == move) {
                    return action
                }
            }
            return null
        }
    }
}
