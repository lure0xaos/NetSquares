package gargoyle.netsquares.model;

import java.util.Arrays;

import static gargoyle.netsquares.model.Direction.DOWN;
import static gargoyle.netsquares.model.Direction.LEFT;
import static gargoyle.netsquares.model.Direction.RIGHT;
import static gargoyle.netsquares.model.Direction.UP;

public enum Directions {
    VERTICALS(UP, DOWN),
    HORIZONTALS(LEFT, RIGHT);
    private final Direction[] directions;

    Directions(Direction... directions) {
        this.directions = directions.clone();
    }

    public Direction[] getDirections() {
        return directions.clone();
    }

    public Direction[] getReachable(int x1, int y1, int x2, int y2) {
        for (Direction direction : getDirections()) {
            if (direction.isReachable(x1, y1, x2, y2)) {
                Direction[] dirs = new Direction[direction.getReachable(x1, y1, x2, y2)];
                Arrays.fill(dirs, direction);
                return dirs;
            }
        }
        return null;
    }

    public boolean isAllowed(Direction direction) {
        return Arrays.stream(directions).anyMatch(dir -> direction == dir);
    }

    public boolean isReachable(int x1, int y1, int x2, int y2) {
        for (Direction direction : getDirections()) {
            if (direction.isReachable(x1, y1, x2, y2)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Directions{directions=%s}", Arrays.toString(directions));
    }
}
