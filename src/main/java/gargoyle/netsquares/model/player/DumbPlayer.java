package gargoyle.netsquares.model.player;

import gargoyle.netsquares.model.Board;
import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.Player;
import org.jetbrains.annotations.NotNull;

public class DumbPlayer extends Player {
    public DumbPlayer(String name) {
        super(name);
    }

    @Override
    protected Move move(@NotNull Board board, @NotNull Directions directions) {
        boolean canOpen = board.canOpen();
        int openScore = board.getAtCaret();
        int max = canOpen ? openScore : Integer.MIN_VALUE;
        Move move = canOpen ? Move.OPEN : null;
        for (Direction direction : directions.getDirections()) {
            for (int i = 1; board.canMoveBy(direction, i); i++) {
                if (board.canOpenBy(direction, i)) {
                    int score = board.getBy(direction, i);
                    if ((max == score && move == null) || max < score) {
                        max = score;
                        move = Move.forDirection(direction);
                    }
                }
            }
        }
        return canOpen && max == openScore ? Move.OPEN : move;
    }
}
