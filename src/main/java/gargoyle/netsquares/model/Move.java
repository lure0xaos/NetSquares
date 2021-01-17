package gargoyle.netsquares.model;

public class Move {
    private Direction direction;
    private int distance;

    public Move(final Direction direction, final int distance) {
        super();
        setDirection(direction);
        setDistance(distance);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    private void setDirection(final Direction direction) {
        this.direction = direction;
    }

    private void setDistance(final int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + ("direction=" + getDirection()) + ","
                + ("distance=" + getDistance()) + "]";
    }
}
