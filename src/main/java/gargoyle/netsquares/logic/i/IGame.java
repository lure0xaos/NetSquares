package gargoyle.netsquares.logic.i;

import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.model.i.IBoardView;
import gargoyle.netsquares.player.a.Player;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public interface IGame {
    Color BAD = Color.RED;
    Color GOOD = Color.GREEN;

    void addGameNotifier(IGameNotifier notifier);

    void clearPlayers();

    List<Player> game(final List<Player> players);

    IBoardView getBoard();

    Color getColor(boolean b);

    Color getColor(long score);

    Image getCover();

    void setCover(String name) throws IOException;

    Player getCurrentPlayer();

    List<Player> getPlayers();

    void setPlayers(List<Player> players);

    List<Player> getWinner(Player resigned);

    boolean isInGame();

    void nextPlayer();

    void playerMove(Player player);

    boolean playerMoving();

    boolean playerMoving(Player player);

    void randomPlayer();

    void removeGameNotifier(IGameNotifier notifier);

    void removeGameNotifiers();

    void setBoard(int size, int score);

    void stopGame();
}
