package gargoyle.netsquares.model;

public enum Move {
    UP(Direction.UP),
    DOWN(Direction.DOWN),
    LEFT(Direction.LEFT),
    RIGHT(Direction.RIGHT),
    OPEN(null);
    private final Direction direction;

    Move(Direction direction) {
        this.direction = direction;
    }

    public static Move forDirection(Direction direction) {
        for (Move move : values()) {
            if (move.direction == direction) {
                return move;
            }
        }
        return null;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isAllowed(Directions allowed) {
        return this == OPEN || allowed.isAllowed(direction);
    }

    @Override
    public String toString() {
        return String.format("Move.%s(%s)", name(), direction);
    }
}
