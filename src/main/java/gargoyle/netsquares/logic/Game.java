package gargoyle.netsquares.logic;

import gargoyle.netsquares.beans.Resources;
import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.GameNotifier;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.logic.i.IGame;
import gargoyle.netsquares.model.Board;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.i.IBoard;
import gargoyle.netsquares.player.a.Player;
import gargoyle.netsquares.util.Images;
import gargoyle.netsquares.util.Randoms;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements IGame {
    private static final Color NEUTRAL = Color.GRAY;
    private static final long movePause = 300;
    private final GameNotifier notifier = new GameNotifier();
    private IBoard board;
    private Image cover;
    private int player;
    private List<Player> players = new ArrayList<Player>();
    private volatile boolean stopped;

    @Override
    public void addGameNotifier(final IGameNotifier notifier) {
        this.notifier.addGameNotifier(notifier);
    }

    @Override
    public void clearPlayers() {
        for (final Player player : players) {
            player.clearScore();
        }
    }

    @Override
    public List<Player> game(final List<Player> players) {
        stopped = false;
        board.reinit();
        setPlayers(players);
        clearPlayers();
        randomPlayer();
        notifier.notify(new GameNote(this, GameNoteType.NEW_GAME));
        boolean normal = false;
        while (!stopped && playerMoving()) {
            nextPlayer();
            notifier.notify(new GameNote(this, GameNoteType.PLAYER_MOVED));
            if (!board.isMoveAvailable(getCurrentPlayer().getAllowedDirections().getDirections())) {
                normal = true;
                break;
            }
        }
        board.open();
        notifier.notify(new GameNote(this, GameNoteType.GAME_OVER));
        return getWinner(normal ? null : getCurrentPlayer());
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public Color getColor(final boolean b) {
        return b ? IGame.GOOD : IGame.BAD;
    }

    @Override
    public Color getColor(final long score) {
        return score == 0 ? NEUTRAL : getColor(score > 0);
    }

    @Override
    public Image getCover() {
        return cover;
    }

    @Override
    public void setCover(final String name) throws IOException {
        try {
            cover = Images.makeRectangular(Resources.forCurrentThread().loadImage("gargoyle/netsquares/" + name));
        } catch (final ClassNotFoundException e) {
            throw new IOException(e);
        }
        notifier.notify(new GameNote(this, GameNoteType.GAME_COVER));
    }

    @Override
    public Player getCurrentPlayer() {
        try {
            return player < players.size() ? players.get(player) : null;
        } catch (final NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public void setPlayers(final List<Player> players) {
        this.players = players;
    }

    @Override
    public List<Player> getWinner(final Player resigned) {
        final List<Player> winners = new ArrayList<Player>();
        long winScore = Integer.MIN_VALUE;
        for (final Player player : players) {
            final long score = player.getScore();
            if (score >= winScore) {
                if (score > winScore) {
                    winners.clear();
                }
                if (player != resigned) {
                    winners.add(player);
                }
                winScore = score;
            }
        }
        return winners;
    }

    @Override
    public boolean isInGame() {
        return !players.isEmpty() && getCurrentPlayer() != null;
    }

    @Override
    public void nextPlayer() {
        player = (player + 1) % players.size();
    }

    @Override
    public void playerMove(final Player player) {
        final int cell = board.getCellAtCaret();
        player.addScore(cell);
        board.openCellAtCaret();
    }

    @Override
    public boolean playerMoving() {
        return playerMoving(getCurrentPlayer());
    }

    @Override
    public boolean playerMoving(final Player player) {
        while (!stopped) {
            try {
                Thread.sleep(movePause);
            } catch (final InterruptedException e) {
                // no pause, ok
            }
            final Move move = player.move(board.clone());
            if (move == null) {
                return false;
            }
            if (move.getDistance() == 0) {
                if (board.getCellAtCaret() == 0) {
                    return false;
                }
                playerMove(player);
                notifier.notify(new GameNote(this, GameNoteType.PLAYER_MOVED));
                return true;
            }
            if (board.isMoveLegal(move, player.getAllowedDirections().getDirections())) {
                board.applyMove(move);
                notifier.notify(new GameNote(this, GameNoteType.PLAYER_MOVING));
            }
        }
        return false;
    }

    @Override
    public void randomPlayer() {
        player = Randoms.random(0, players.size());
    }

    @Override
    public void removeGameNotifier(final IGameNotifier notifier) {
        this.notifier.removeGameNotifier(notifier);
    }

    @Override
    public void removeGameNotifiers() {
        notifier.removeGameNotifiers();
    }

    @Override
    public void setBoard(final int size, final int score) {
        board = new Board(size, score);
    }

    @Override
    public void stopGame() {
        stopped = true;
        notifier.notify(new GameNote(this, GameNoteType.GAME_OVER));
    }
}
