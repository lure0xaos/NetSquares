package gargoyle.netsquares.model;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum GameState {
    MENU,
    GAME,
    OPEN;

    public boolean isInState(@NotNull GameState... states) {
        return Arrays.stream(states).anyMatch(gameState -> gameState == this);
    }
}
