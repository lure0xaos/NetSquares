package gargoyle.netsquares.model.i;

import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Move;

public interface IBoard extends IBoardView {
    void applyMove(Move move);

    void initScore(int score);

    void initSize(int size);

    boolean move(Direction direction);

    void open();

    int openCellAtCaret();

    void reinit();
}
