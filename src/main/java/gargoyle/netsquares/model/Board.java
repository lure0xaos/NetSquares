package gargoyle.netsquares.model;

import gargoyle.netsquares.util.Assert;
import gargoyle.netsquares.util.Rnd;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;

public final class Board implements Serializable, Cloneable {
    private static final int[][] NO_CELLS = new int[0][0];
    private int caretX;
    private int caretY;
    private int[][] cells;
    private int left;
    private int size;
    private int weight;

    public Board() {
        cells = NO_CELLS;
        size = 0;
        weight = 0;
        left = 0;
        caretX = 0;
        caretY = 0;
    }

    public boolean canMoveBy(@NotNull Move move) {
        return move == Move.OPEN || canMoveBy(Objects.requireNonNull(move.getDirection()));
    }

    private boolean canMoveBy(@NotNull Direction direction) {
        Assert.assertNotNull(direction, "direction is null");
        return canMoveBy(direction.getDeltaX(), direction.getDeltaY());
    }

    public boolean canMoveBy(@NotNull Direction direction, int step) {
        Assert.assertNotNull(direction, "direction is null");
        return canMoveBy(direction.getDeltaX() * step, direction.getDeltaY() * step);
    }

    private boolean canMoveBy(int deltaX, int deltaY) {
        return isIn(caretX + deltaX, caretY + deltaY);
    }

    public boolean canOpen() {
        return !isOpenAtCaret();
    }

    public boolean canOpenBy(@NotNull Move move) {
        return (move == Move.OPEN && canOpen()) || canOpenBy(Objects.requireNonNull(move.getDirection()));
    }

    private boolean canOpenBy(@NotNull Direction direction) {
        Assert.assertNotNull(direction, "direction is null");
        return canOpenBy(direction.getDeltaX(), direction.getDeltaY());
    }

    public boolean canOpenBy(@NotNull Direction direction, int step) {
        Assert.assertNotNull(direction, "direction is null");
        return canOpenBy(direction.getDeltaX() * step, direction.getDeltaY() * step);
    }

    private boolean canOpenBy(int deltaX, int deltaY) {
        return canMoveBy(deltaX, deltaY) && !isOpenBy(deltaX, deltaY);
    }

    public void each(@NotNull BiFunction<Integer, Integer, Integer> operation) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Integer res = operation.apply(x, y);
                if (res != null) {
                    cells[x][y] = res;
                }
            }
        }
    }

    public int getAt(int x, int y) {
        return isIn(x, y) ? cells[x][y] : 0;
    }

    public int getAtCaret() {
        return getAt(caretX, caretY);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return caretX == board.caretX &&
                caretY == board.caretY &&
                left == board.left &&
                size == board.size &&
                weight == board.weight &&
                Arrays.equals(cells, board.cells);
    }

    @Override
    public String toString() {
        return String.format("Board{size=%d, weight=%d, caretX=%d, caretY=%d, left=%d, cells=%s}", size, weight, caretX, caretY, left, Arrays.deepToString(cells));
    }

    private int getBy(int deltaX, int deltaY) {
        return getAt(caretX + deltaX, caretY + deltaY);
    }

    private int getBy(@NotNull Direction direction) {
        Assert.assertNotNull(direction, "direction is null");
        return getBy(direction.getDeltaX(), direction.getDeltaY());
    }

    public int getCaretX() {
        return caretX;
    }

    public int getCaretY() {
        return caretY;
    }

    public int getLeft() {
        return left;
    }

    public int getSize() {
        return size;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(caretX, caretY, left, size, weight);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }

    public int getBy(@NotNull Direction direction, int step) {
        Assert.assertNotNull(direction, "direction is null");
        return getBy(direction.getDeltaX() * step, direction.getDeltaY() * step);
    }

    @Override
    public Board clone() {
        Board board;
        try {
            board = (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        board.caretX = caretX;
        board.caretY = caretY;
        board.left = left;
        board.size = size;
        board.weight = weight;
        board.each((x, y) -> cells[x][y]);
        return board;
    }

    public int getBy(@NotNull Move move) {
        return move == Move.OPEN ? getAtCaret() : getBy(Objects.requireNonNull(move.getDirection()));
    }

    public void init(int size, int weight) {
        cells = new int[size][size];
        for (int x = 0; x < size; x++) {
            cells[x] = new int[size];
            for (int y = 0; y < size; y++) {
                while (cells[x][y] == 0) {
                    cells[x][y] = Rnd.rnd(-weight, weight);
                }
            }
        }
        this.size = size;
        this.weight = weight;
        left = size * size;
        caretX = size / 2;
        caretY = size / 2;
    }

    public boolean isCaretAt(int x, int y) {
        return x == caretX && y == caretY;
    }

    private boolean isIn(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size;
    }

    public boolean isOpenAt(int x, int y) {
        return cells[x][y] == 0;
    }

    private boolean isOpenAtCaret() {
        return cells[caretX][caretY] == 0;
    }

    public boolean isOpenBy(@NotNull Direction direction, int step) {
        Assert.assertNotNull(direction, "direction is null");
        return isOpenBy(direction.getDeltaX() * step, direction.getDeltaY() * step);
    }

    public boolean isOpenBy(@NotNull Move move) {
        return move == Move.OPEN ? isOpenAtCaret() : isOpenBy(Objects.requireNonNull(move.getDirection()));
    }

    private boolean isOpenBy(@NotNull Direction direction) {
        Assert.assertNotNull(direction, "direction is null");
        return isOpenBy(direction.getDeltaX(), direction.getDeltaY());
    }

    private boolean isOpenBy(int deltaX, int deltaY) {
        int x = caretX + deltaX;
        int y = caretY + deltaY;
        return isIn(x, y) && isOpenAt(x, y);
    }

    public void moveBy(@NotNull Move move) {
        if (move == Move.OPEN) {
            openAtCaret();
        } else {
            moveBy(Objects.requireNonNull(move.getDirection()));
        }
    }

    private void moveBy(@NotNull Direction direction) {
        Assert.assertNotNull(direction, "direction is null");
        moveBy(direction.getDeltaX(), direction.getDeltaY());
    }

    public boolean moveBy(@NotNull Direction direction, int step) {
        Assert.assertNotNull(direction, "direction is null");
        return moveBy(direction.getDeltaX() * step, direction.getDeltaY() * step);
    }

    private boolean moveBy(int deltaX, int deltaY) {
        boolean move = canMoveBy(deltaX, deltaY);
        if (move) {
            caretX += deltaX;
            caretY += deltaY;
        }
        return move;
    }

    public int openAtCaret() {
        int r = cells[caretX][caretY];
        cells[caretX][caretY] = 0;
        if (r != 0) {
            left--;
        }
        return r;
    }

    public void openBoard() {
        each((x, y) -> 0);
        left = 0;
    }

    public void reInit() {
        init(size, weight);
    }
}
