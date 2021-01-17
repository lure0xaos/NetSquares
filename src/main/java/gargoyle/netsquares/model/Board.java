package gargoyle.netsquares.model;

import gargoyle.netsquares.model.i.IBoard;
import gargoyle.netsquares.util.Randoms;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;

public final class Board implements Serializable, Cloneable, IBoard {
    private static final long serialVersionUID = 8850083089471052287L;
    private Point caret;
    private int[][] cells;
    private int score;
    private int size;

    public Board() {
        super();
    }

    public Board(final int width, final int score) {
        this();
        initSize(width);
        initScore(score);
    }

    @Override
    public void applyMove(final Move move) {
        getCaret().setLocation(getDestination(move));
    }

    @Override
    public IBoard clone() {
        final Board clone;
        clone = new Board();
        clone.size = size;
        clone.score = score;
        clone.cells = cells.clone();
        clone.caret = caret == null ? null : new Point(caret);
        return clone;
    }

    @Override
    public int getBoardSize() {
        return size;
    }

    @Override
    public Point getCaret() {
        return caret;
    }

    @Override
    public int getCellAt(final int x, final int y) {
        try {
            return cells[x][y];
        } catch (final ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    @Override
    public int getCellAtCaret() {
        return caret == null ? 0 : getCellAt(caret.x, caret.y);
    }

    @Override
    public int getCellAtDestination(final Move move) {
        return getCellAtDestination(getCaret(), move);
    }

    @Override
    public int getCellAtDestination(final Point point, final Move move) {
        final Point c = getDestination(point, move);
        return getCellAt(c.x, c.y);
    }

    @Override
    public Point getDestination(final Move move) {
        return getDestination(getCaret(), move);
    }

    @Override
    public Point getDestination(final Point point, final Move move) {
        final Point destination = new Point(point);
        if (move.getDirection() == Direction.DOWN) {
            destination.y += move.getDistance();
        }
        if (move.getDirection() == Direction.LEFT) {
            destination.x -= move.getDistance();
        }
        if (move.getDirection() == Direction.RIGHT) {
            destination.x += move.getDistance();
        }
        if (move.getDirection() == Direction.UP) {
            destination.y -= move.getDistance();
        }
        return destination;
    }

    @Override
    public Move getMoveToPoint(final int x, final int y) {
        final int dx = x - caret.x;
        final int dy = y - caret.y;
        if ((dx > 0) && (dy == 0)) {
            return new Move(Direction.RIGHT, dx);
        }
        if ((dx < 0) && (dy == 0)) {
            return new Move(Direction.LEFT, -dx);
        }
        if ((dx == 0) && (dy > 0)) {
            return new Move(Direction.DOWN, dy);
        }
        if ((dx == 0) && (dy < 0)) {
            return new Move(Direction.UP, -dy);
        }
        return null;
    }

    @Override
    public Move getNearestMove(final Direction direction) {
        int distance = 1;
        Move move;
        while (isMoveIn(move = new Move(direction, distance))) {
            if (isMoveLegal(move, null)) {
                return move;
            }
            distance++;
        }
        return null;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void initScore(final int score) {
        this.score = score;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                while (isOpenAt(x, y)) {
                    final int a = -score;
                    cells[x][y] = Randoms.random(a, score);
                }
            }
        }
    }

    @Override
    public void initSize(final int size) {
        this.size = size;
        cells = new int[size][size];
        caret = new Point(size / 2, size / 2);
    }

    @Override
    public boolean isIn(final int x, final int y) {
        return (x >= 0) && (x < size) && (y >= 0) && (y < size);
    }

    @Override
    public boolean isMoveAvailable(final Direction[] allowedDirections) {
        for (final Direction direction : allowedDirections) {
            int distance = 1;
            Move move;
            while (isMoveIn(move = new Move(direction, distance))) {
                if (isMoveLegal(move, allowedDirections)) {
                    return true;
                }
                distance++;
            }
        }
        return false;
    }

    @Override
    public boolean isMoveIn(final Move move) {
        return isMoveInFrom(getCaret(), move);
    }

    @Override
    public boolean isMoveInFrom(final Point point, final Move move) {
        final Point destination = getDestination(point, move);
        return isIn(destination.x, destination.y);
    }

    @Override
    public boolean isMoveLegal(final Move move, final Direction[] allowedDirections) {
        final Direction direction = move.getDirection();
        if ((allowedDirections == null) || (allowedDirections.length == 0 || (direction == null))
                || Arrays.asList(allowedDirections).contains(direction)) {
            final Point newCaret = getDestination(move);
            return !isOpenAt(newCaret.x, newCaret.y);
        }
        return false;
    }

    @Override
    public boolean isOpenAt(final int x, final int y) {
        return getCellAt(x, y) == 0;
    }

    @Override
    public boolean isOpenAtCaret() {
        return (caret == null) || isOpenAt(caret.x, caret.y);
    }

    @Override
    public boolean isOpen() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!isOpenAt(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean move(final Direction direction) {
        final Point c = new Point(getCaret());
        do {
            if (direction == Direction.UP) {
                if (c.y <= 0) {
                    return false;
                }
                c.y--;
            }
            if (direction == Direction.DOWN) {
                if (c.y >= (size - 1)) {
                    return false;
                }
                c.y++;
            }
            if (direction == Direction.LEFT) {
                if (c.x <= 0) {
                    return false;
                }
                c.x--;
            }
            if (direction == Direction.RIGHT) {
                if (c.x >= (size - 1)) {
                    return false;
                }
                c.x++;
            }
        } while (getCellAt(c.x, c.y) == 0);
        caret.setLocation(c);
        return true;
    }

    @Override
    public void open() {
        caret = null;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                cells[x][y] = 0;
            }
        }
    }

    private int openCell(final int x, final int y) {
        final int cell = getCellAt(x, y);
        try {
            cells[x][y] = 0;
        } catch (final ArrayIndexOutOfBoundsException e) {
            return cell;
        }
        return cell;
    }

    @Override
    public int openCellAtCaret() {
        return caret == null ? 0 : openCell(caret.x, caret.y);
    }

    @Override
    public void reinit() {
        initScore(score);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        final Board board = (Board) o;

        if (score != board.score) return false;
        if (size != board.size) return false;
        return Arrays.deepEquals(cells, board.cells);

    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(cells);
        result = 31 * result + score;
        result = 31 * result + size;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Board{");
        sb.append("size=").append(size);
        sb.append(", score=").append(score);
        sb.append(", caret=").append(caret);
        sb.append(", cells=").append(Arrays.toString(cells));
        sb.append('}');
        return sb.toString();
    }
}
