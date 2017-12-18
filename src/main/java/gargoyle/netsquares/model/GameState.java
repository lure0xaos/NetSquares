package gargoyle.netsquares.model;

import java.util.Arrays;

public enum GameState {
    MENU,
    GAME,
    OPEN;

    public boolean isInState(GameState... states) {
        return Arrays.stream(states).anyMatch(gameState -> gameState == this);
    }
}
