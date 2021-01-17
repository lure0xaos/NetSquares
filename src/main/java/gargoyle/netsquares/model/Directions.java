package gargoyle.netsquares.model;

public enum Directions {
    ALL(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN), HORIZONTAL(Direction.LEFT,
            Direction.RIGHT), VERTICAL(Direction.UP, Direction.DOWN);
    private final Direction[] directions;

    Directions(final Direction... directions) {
        this.directions = directions;
    }

    public boolean contains(final Direction direction) {
        for (final Direction dir : directions) {
            if (dir == direction) {
                return true;
            }
        }
        return false;
    }

    public Direction[] getDirections() {
        return directions.clone();
    }
}
