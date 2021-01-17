package gargoyle.netsquares.player;

import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.i.IBoardView;
import gargoyle.netsquares.player.a.Player;

import java.awt.*;

public class ComputerPlayer extends Player {
    private Point bestDestination;

    public ComputerPlayer(final String name, final Directions allowedDirections) {
        super(name, allowedDirections);
    }

    private Point getBestDestination(final IBoardView board, final int deep) {
        return getBestDestination(board.getCaret(), board, deep, deep, true);
    }

    private Point getBestDestination(final Point caret, final IBoardView board, final int deep, final int maxDepth,
                                     final boolean my) {
        double score = Integer.MIN_VALUE;
        Point destination = new Point(caret);
        Move move;
        final Directions allowedDirections = getAllowedDirections();
        for (final Direction direction : Directions.ALL.getDirections()) {
            final boolean allowed = allowedDirections.contains(direction);
            if (!((my && allowed) || (!my && !allowed))) {
                continue;
            }
            int distance = 1;
            while (board.isMoveInFrom(caret, move = new Move(direction, distance))) {
                final Point candidate = board.getDestination(caret, move);
                final double newScore0 = board.getCellAtDestination(caret, move);
                if (newScore0 != 0) {
                    final Point peerDestination = deep > 0
                            ? getBestDestination(candidate, board, deep - 1, maxDepth, !my) : null;
                    final double newScore1 = peerDestination == null ? 0
                            : board.getCellAt(peerDestination.x, peerDestination.y);
                    final double newScore = newScore0
                            - (newScore1 == Integer.MIN_VALUE ? 0 : newScore1 * (maxDepth - deep));
                    if ((newScore != 0) && (newScore > score)) {
                        score = newScore;
                        destination = candidate;
                    }
                }
                distance++;
            }
        }
        return destination;
    }

    /**
     * @param board - copy of game board
     * @return Move to visualize moving, null to resign, Move.distance=0 to
     * confirm last move
     */
    @Override
    public Move move(final IBoardView board) {
        if (bestDestination == null) {
            bestDestination = getBestDestination(board, 3);
            if (bestDestination == null) {
                return null;
            }
        }
        if (board.getCaret().equals(bestDestination)) {
            bestDestination = null;
            return new Move(null, 0);
        }
        return board
                .getNearestMove(board.getMoveToPoint(bestDestination.x, bestDestination.y).getDirection());
    }
}
