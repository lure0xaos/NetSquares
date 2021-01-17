package gargoyle.netsquares.player.a;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.i.IBoardView;

public abstract class Player implements IGameNotifier {
    private final Directions allowedDirections;
    private final String name;
    private long score;

    public Player(final String name, final Directions allowedDirections) {
        this.name = name;
        this.allowedDirections = allowedDirections;
    }

    public void addScore(final long score) {
        this.score += score;
    }

    public void clearScore() {
        score = 0;
    }

    public final Directions getAllowedDirections() {
        return allowedDirections;
    }

    public String getName() {
        return name;
    }

    public long getScore() {
        return score;
    }

    /**
     * @param board - copy of game board
     * @return Move to visualize moving, null to resign, Move.distance=0 to
     * confirm last move
     */
    public abstract Move move(final IBoardView board);

    @Override
    public GameNote notify(final GameNote note) {
        return null;
    }

    @Override
    public final String toString() {
        return getClass().getSimpleName() + " \"" + getName() + "\": " + getScore();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        final Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
