package gargoyle.netsquares.model

enum class GameState {
    MENU, GAME, OPEN;

    fun isInState(vararg states: GameState): Boolean =
        states.any { it == this }
}
