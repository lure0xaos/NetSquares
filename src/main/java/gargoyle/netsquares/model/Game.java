package gargoyle.netsquares.model;

import gargoyle.netsquares.res.AudioClip;
import gargoyle.netsquares.res.NSContext;
import gargoyle.netsquares.store.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Game {
    private static final String RES_BG = "sound/bg/au/Frunze-Evenings";
    private static final String RES_TAP = "sound/fx/tap";
    private final Board board;
    private final AudioClip mus;
    private final Options options;
    private final List<Player> players;
    private final AudioClip tap;
    private int current;
    private Map<Player, Directions> playerDirectionsMap;
    private GameState state;

    public Game(NSContext context, Options options) {
        this(context, options, new Board(), GameState.MENU);
    }

    private Game(NSContext context, Options options, Board board, GameState state, Player... players) {
        this(context, options, board, Arrays.asList(players), state);
    }

    private Game(NSContext context, Options options, Board board, List<Player> players, GameState state) {
        this.options = options;
        this.state = state;
        this.board = board;
        this.players = new ArrayList<>();
        tap = context.loadAudio(RES_TAP, "wav");
        mus = context.loadAudio(RES_BG, "au");
        initPlayers(players);
    }

    private void initPlayers(List<Player> players) {
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

    public static Game createGame(NSContext context, Options options, Board board, Player... players) {
        return new Game(context, options, board, GameState.GAME, players).beginGame();
    }

    private Game beginGame() {
        resetPlayers();
        board.reInit();
        state = GameState.GAME;
        return this;
    }

    private void beginGame(Player... players) {
        initPlayers(Arrays.asList(players));
        board.reInit();
        state = GameState.GAME;
        if (options.isSound() && options.isMusic()) {
            musicStart();
        }
    }

    public void beginGame(int boardSize, int boardWeight, Player... players) {
        board.init(boardSize, boardWeight);
        beginGame(players);
    }

    public void musicStart() {
        mus.loop();
    }

    public Player computeWinner() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return current == game.current &&
                Objects.equals(board, game.board) &&
                Objects.equals(players, game.players) &&
                state == game.state;
    }

    public void endGame() {
        if (GameState.GAME.isInState(state)) {
            state = GameState.OPEN;
            board.openBoard();
        }
        musicStop();
    }

    public Board getBoard() {
        return board;
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

    public boolean move() {
        if (GameState.GAME.isInState(state) && canMove()) {
            makeMove();
            return true;
        }
        return false;
    }

    private boolean canMove() {
        return canMove(getCurrentPlayer());
    }

    @Override
    public String toString() {
        return String.format("Game{state=%s, current=%d, playerDirectionsMap=%s, players=%s, board=%s}", state, current, playerDirectionsMap, players, board);
    }

    public boolean isAllowed(Player player, Direction direction) {
        return direction.isAllowed(getAllowed(player));
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

    public boolean isState(GameState state) {
        return this.state == state;
    }

    public boolean isState(GameState... states) {
        return this.state.isInState(states);
    }

    public Player getCurrentPlayer() {
        return players.isEmpty() ? null : players.get(current);
    }

    public Directions getAllowed(Player player) {
        return playerDirectionsMap.get(player);
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

    public void musicStop() {
        mus.stop();
    }

    private void resetPlayers() {
        for (Player player : players) {
            player.reset();
        }
    }

    private void nextPlayer() {
        current = (current + 1) % players.size();
    }
}
