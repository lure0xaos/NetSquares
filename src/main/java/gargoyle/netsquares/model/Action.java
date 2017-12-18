package gargoyle.netsquares.model;

import org.jetbrains.annotations.Nullable;

public enum Action {
    UP(Move.UP),
    DOWN(Move.DOWN),
    LEFT(Move.LEFT),
    RIGHT(Move.RIGHT),
    FIRE(Move.OPEN),;
    private final Move move;

    Action(Move move) {
        this.move = move;
    }

    public static @Nullable Action forMove(Move move) {
        for (Action action : values()) {
            if (action.move == move) {
                return action;
            }
        }
        return null;
    }

    public Move getMove() {
        return move;
    }
}
