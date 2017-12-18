package gargoyle.netsquares.model;

public enum Action {
    UP(Move.UP),
    DOWN(Move.DOWN),
    LEFT(Move.LEFT),
    RIGHT(Move.RIGHT),
    FIRE(Move.OPEN),
    ;
    private final Move move;

    Action(Move move) {
        this.move = move;
    }

    public static Action forMove(Move move) {
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
