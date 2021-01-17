package gargoyle.netsquares.player;

import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.i.IBoardView;
import gargoyle.netsquares.player.a.Player;

public class NullPlayer extends Player {
    public NullPlayer(final String name, final Directions allowedDirections) {
        super(name, allowedDirections);
    }

    @Override
    public Move move(final IBoardView board) {
        return new Move(null, 0);
    }
}
