package gargoyle.netsquares.model;

import gargoyle.netsquares.res.AudioClip;
import gargoyle.netsquares.res.Resources;
import gargoyle.netsquares.store.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class Game implements Serializable, Cloneable {
    private static final String RES_BG = "sound/bg/au/Frunze-Evenings.au";
    private static final String RES_TAP = "sound/fx/tap.wav";
    private final Board board;
    private final AudioClip mus;
    private final Options options;
    private final AudioClip tap;
    private int current;
    private Map<Player, Directions> playerDirectionsMap;
    private final List<Player> players;
    private GameState state;

    public Game(Options options) {
        this.options = options;
        state = GameState.MENU;
        board = new Board();
        players = new ArrayList<>();
        playerDirectionsMap = new HashMap<>();
        tap = Resources.loadAudio(RES_TAP, Locale.getDefault());
        mus = Resources.loadAudio(RES_BG, Locale.getDefault());
    }

    private Game(Options options, Board board, Player... players) {
        this(options, board, Arrays.asList(players));
    }

    private Game(Options options, Board board, @NotNull List<Player> players) {
        this.options = options;
        this.board = board;
        this.state = GameState.GAME;
        this.players = new ArrayList<>();
        initPlayers(players);
        tap = Resources.loadAudio(RES_TAP, Locale.getDefault());
        mus = Resources.loadAudio(RES_BG, Locale.getDefault());
    }

    public static @NotNull Game createGame(Options options, Board board, Player... players) {
        return new Game(options, board, players).beginGame();
    }

    private void beginGame(Player... players) {
        initPlayers(Arrays.asList(players));
        board.reInit();
        state = GameState.GAME;
        if (options.isSound() && options.isMusic()) {
            musicStart();
        }
    }

    @NotNull
    private Game beginGame() {
        resetPlayers();
        board.reInit();
        state = GameState.GAME;
        return this;
    }

    public void beginGame(int boardSize, int boardWeight, Player... players) {
        board.init(boardSize, boardWeight);
        beginGame(players);
    }

    private boolean canMove() {
        return canMove(getCurrentPlayer());
    }

    @SuppressWarnings("LawOfDemeter")
    private boolean canMove(Player player) {
        if (board.canOpen()) {
            return true;
        }
        Directions directions = getAllowed(player);
        for (Direction direction : directions.getDirections()) {
            for (int i = 1; board.canMoveBy(direction, i); i++) {
                if (board.canOpenBy(direction, i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public @Nullable Player computeWinner() {
        int maxScore = Integer.MIN_VALUE;
        Player winner = null;
        for (Player player : players) {
            int score = player.getScore();
            if (maxScore < score) {
                maxScore = score;
                winner = player;
            }
        }
        return winner;
    }

    public void endGame() {
        if (GameState.GAME.isInState(state)) {
            state = GameState.OPEN;
            board.openBoard();
        }
        musicStop();
    }

    public Directions getAllowed(@NotNull Player player) {
        return playerDirectionsMap.get(player);
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players.isEmpty() ? null : players.get(current);
    }

    public List<Player> getPlayers() {
        //noinspection AssignmentOrReturnOfFieldWithMutableType
        return players;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, players, current, state);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return current == game.current &&
                Objects.equals(board, game.board) &&
                Objects.equals(players, game.players) &&
                state == game.state;
    }

    @Override
    public @NotNull Game clone() throws CloneNotSupportedException {
        return (Game) super.clone();
    }

    @Override
    public String toString() {
        return String.format("Game{state=%s, current=%d, playerDirectionsMap=%s, players=%s, board=%s}", state, current, playerDirectionsMap, players, board);
    }

    private void initPlayers(@NotNull List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
        Map<Player, Directions> playerDirectionsMap = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Directions directions = i % 2 == 0 ? Directions.HORIZONTALS : Directions.VERTICALS;
            playerDirectionsMap.put(player, directions);
        }
        this.playerDirectionsMap = Collections.unmodifiableMap(playerDirectionsMap);
    }

    public boolean isAllowed(Player player, Direction direction) {
        return direction.isAllowed(getAllowed(player));
    }

    public boolean isState(GameState state) {
        return this.state == state;
    }

    public boolean isState(GameState... states) {
        return this.state.isInState(states);
    }

    @SuppressWarnings("LawOfDemeter")
    private void makeMove() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }
        Move move = currentPlayer.move(board, getAllowed(currentPlayer));
        if (move == null) {
            return;
        }
        if (options.isSound()) {
            tap.play();
        }
        if (move == Move.OPEN) {
            currentPlayer.addScore(board.openAtCaret());
            nextPlayer();
            return;
        }
        if (getAllowed(currentPlayer).isAllowed(move.getDirection()) && board.canMoveBy(move)) {
            board.moveBy(move);
        }
    }

    public boolean move() {
        if (GameState.GAME.isInState(state) && canMove()) {
            makeMove();
            return true;
        }
        return false;
    }

    public void musicStart() {
        mus.loop();
    }

    public void musicStop() {
        mus.stop();
    }

    private void nextPlayer() {
        current = (current + 1) % players.size();
    }

    private void resetPlayers() {
        for (Player player : players) {
            player.reset();
        }
    }
}
