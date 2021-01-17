package gargoyle.netsquares.model.i;

import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Move;

import java.awt.*;

public interface IBoardView {
    IBoard clone();

    int getBoardSize();

    Point getCaret();

    int getCellAt(int x, int y);

    int getCellAtCaret();

    int getCellAtDestination(Move move);

    int getCellAtDestination(Point point, Move move);

    Point getDestination(Move move);

    Point getDestination(Point point, Move move);

    Move getMoveToPoint(int x, int y);

    Move getNearestMove(Direction direction);

    int getScore();

    boolean isIn(int x, int y);

    boolean isMoveAvailable(Direction[] allowedDirections);

    boolean isMoveIn(Move move);

    boolean isMoveInFrom(Point point, Move move);

    boolean isMoveLegal(Move move, Direction[] allowedDirections);

    boolean isOpenAt(int x, int y);

    boolean isOpenAtCaret();

    boolean isOpen();
}
