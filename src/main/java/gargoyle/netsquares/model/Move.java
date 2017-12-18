package gargoyle.netsquares.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Move {
    UP(Direction.UP),
    DOWN(Direction.DOWN),
    LEFT(Direction.LEFT),
    RIGHT(Direction.RIGHT),
    OPEN(null);
    private final @Nullable Direction direction;

    Move(@Nullable Direction direction) {
        this.direction = direction;
    }

    public static @Nullable Move forDirection(Direction direction) {
        for (Move move : values()) {
            if (move.direction == direction) {
                return move;
            }
        }
        return null;
    }

    public @Nullable Direction getDirection() {
        return direction;
    }

    public boolean isAllowed(@NotNull Directions allowed) {
        return this == OPEN || allowed.isAllowed(direction);
    }

    @Override
    public String toString() {
        return String.format("Move.%s(%s)", name(), direction);
    }
}
