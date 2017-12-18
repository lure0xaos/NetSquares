package gargoyle.netsquares.model;

import org.jetbrains.annotations.NotNull;

public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);
    private final int deltaX;
    private final int deltaY;

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    @SuppressWarnings("UseCompareMethod")
    private static int sgn(int v) {
        return v == 0 ? 0 : (v < 0 ? -1 : 1);
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public int getReachable(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        if ((sgn(dx) == deltaX) && (sgn(dy) == deltaY)) {
            return (deltaX == 0 ? 0 : (dx / deltaX)) + (deltaY == 0 ? 0 : (dy / deltaY));
        }
        return -1;
    }

    public boolean isAllowed(@NotNull Directions allowed) {
        return allowed.isAllowed(this);
    }

    public boolean isHorizontal() {
        return deltaY == 0;
    }

    public boolean isReachable(int x1, int y1, int x2, int y2) {
        return (sgn(x2 - x1) == deltaX) && (sgn(y2 - y1) == deltaY);
    }

    public boolean isVertical() {
        return deltaX == 0;
    }

    @Override
    public String toString() {
        return String.format("Direction(%s):%d,%d", name(), deltaX, deltaY);
    }
}
