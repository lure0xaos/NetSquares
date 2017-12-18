package gargoyle.netsquares.ui.painter;

import gargoyle.netsquares.i18n.Messages;
import gargoyle.netsquares.model.Board;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Game;
import gargoyle.netsquares.model.GameState;
import gargoyle.netsquares.model.Player;
import gargoyle.netsquares.res.Resources;
import gargoyle.netsquares.ui.painter.state.GraphicsStateManager;
import gargoyle.netsquares.util.InterpolUtils;
import gargoyle.netsquares.util.SwingDraw;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NSPainter implements Serializable {
    private static final int CARET_WIDTH_MAX = 10;
    private static final int CARET_WIDTH_MIN = 5;
    private static final Color CELL_BORDER = Color.BLACK;
    private static final Color CELL_CARET = Color.BLUE;
    private static final Color CELL_LABEL = Color.BLACK;
    private static final Color CELL_NEGATIVE = Color.RED;
    private static final Color CELL_NEGATIVE_HIGHLIGHT = InterpolUtils.interpolate(CELL_NEGATIVE, Color.WHITE, 2, 1);
    private static final Color CELL_POSITIVE = Color.GREEN;
    private static final Color CELL_POSITIVE_HIGHLIGHT = InterpolUtils.interpolate(CELL_POSITIVE, Color.WHITE, 2, 1);
    private static final Color INFOBAR_BG = Color.BLACK;
    private static final Color INFOBAR_BORDER = Color.BLUE;
    private static final Color INFOBAR_NEGATIVE = Color.RED;
    private static final Color INFOBAR_PLAYER = Color.WHITE;
    private static final Color INFOBAR_PLAYER_MOVE = Color.GREEN;
    private static final Color INFOBAR_PLAYER_WAIT = Color.RED;
    private static final Color INFOBAR_POSITIVE = Color.GREEN;
    private static final String MSG_INFOBAR_STATUS_MOVE = "infobar.status.move";
    private static final String MSG_INFOBAR_STATUS_WAIT = "infobar.status.wait";
    private static final String RES_FONT_LCD = "font/lcd/digital-7.ttf";
    private static final String RES_FONT_ZX = "font/zx/zx_spectrum-7.ttf";
    private final Font fontLCD;
    private final Font fontZX;

    public NSPainter(Locale locale) {
        fontLCD = Resources.loadFont(RES_FONT_LCD, locale);
        fontZX = Resources.loadFont(RES_FONT_ZX, locale);
    }

    public Point getCellAt(@NotNull Rectangle2D bounds, int boardSize, double x, double y) {
        return new Point((int) ((x - bounds.getX()) * boardSize / bounds.getWidth()), (int) ((y - bounds.getY()) * boardSize / bounds.getHeight()));
    }

    public Rectangle2D.Double getCellBounds(@NotNull Rectangle2D bounds, int boardSize, int x, int y) {
        double cellWidth = bounds.getWidth() / boardSize;
        double cellHeight = bounds.getHeight() / boardSize;
        return new Rectangle2D.Double(bounds.getX() + x * cellWidth, bounds.getY() + y * cellHeight, cellWidth, cellHeight);
    }

    public void paintBoard(@NotNull Graphics2D g, @NotNull Game game, @NotNull Rectangle2D bounds) {
        try (GraphicsStateManager manager = new GraphicsStateManager(g, true)) {
            if (game.isState(GameState.GAME)) {
                manager.save();
                paintBoardInGame(manager, g, bounds, game.getBoard(),
                        game.getAllowed(Objects.requireNonNull(game.getCurrentPlayer())));
                manager.restore();
            }
            if (game.isState(GameState.OPEN)) {
                manager.save();
                paintBoardInOpen(manager, g, game, bounds);
                manager.restore();
            }
        }
    }

    private void paintBoardInGame(@NotNull GraphicsStateManager manager, Graphics2D g, @NotNull Rectangle2D bounds, Board board, @NotNull Directions allowed) {
        g.setColor(CELL_BORDER);
        g.fill(bounds);
        board.each((x, y) -> {
            manager.save();
            Rectangle2D.Double cellBounds = getCellBounds(bounds, board.getSize(), x, y);
            g.setClip(cellBounds);
            boolean reachable = allowed.isReachable(board.getCaretX(), board.getCaretY(), x, y);
            paintCellInGame(g, board.isOpenAt(x, y), board.getAt(x, y), board.isCaretAt(x, y), reachable, cellBounds);
            manager.restore();
            return null;
        });
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private void paintBoardInOpen(GraphicsStateManager manager, Graphics2D g, Game game, Rectangle2D bounds) {
        g.setColor(CELL_BORDER);
        g.fill(bounds);
    }

    private void paintCellBorderInGame(@NotNull Graphics2D g, Rectangle2D bounds, @NotNull Color color) {
        int range = InterpolUtils.toRange((int) (bounds.getWidth() / 10), CARET_WIDTH_MIN, CARET_WIDTH_MAX);
        for (int i = 0; i < range; i++) {
            g.setColor(InterpolUtils.interpolate(CELL_BORDER, color, range, i));
            g.draw(new Rectangle2D.Double(bounds.getX() + i, bounds.getY() + i, bounds.getWidth() - i - i, bounds.getHeight() - i - i));
        }
    }

    private void paintCellCaretInGame(@NotNull Graphics2D g, @NotNull Rectangle2D bounds) {
        paintCellBorderInGame(g, bounds, CELL_CARET);
    }

    private void paintCellInGame(@NotNull Graphics2D g, boolean open, int score, boolean caret, boolean reachable, @NotNull Rectangle2D bounds) {
        if (open) {
            if (caret) {
                paintCellCaretInGame(g, bounds);
            }
        } else {
            Color cellBackground = score > 0 ? reachable ? CELL_POSITIVE_HIGHLIGHT : CELL_POSITIVE : reachable ? CELL_NEGATIVE_HIGHLIGHT : CELL_NEGATIVE;
            g.setColor(cellBackground);
            g.fill(bounds);
            paintCellBorderInGame(g, bounds, cellBackground);
            g.setColor(CELL_BORDER);
            g.draw(bounds);
            if (caret) {
                paintCellCaretInGame(g, bounds);
            }
            g.setColor(CELL_LABEL);
            g.setFont(fontZX);
            String label = String.valueOf(Math.abs(score));
            SwingDraw.updateFontSize(g, bounds, label);
            SwingDraw.drawStringCentered(g, bounds, label);
        }
    }

    public void paintInfo(@NotNull Graphics2D g, @NotNull Game game, @NotNull Rectangle2D bounds, @NotNull Messages messages) {
        try (GraphicsStateManager manager = new GraphicsStateManager(g, true)) {
            manager.save();
            GameState state = game.getState();
            if (game.isState(GameState.GAME, GameState.OPEN)) {
                paintInfoInGame(manager, g, bounds, messages, state, game.getPlayers(), game.getCurrentPlayer());
            }
            manager.restore();
        }
    }

    @SuppressWarnings("UnnecessaryCodeBlock")
    private void paintInfoBarInGame(@NotNull GraphicsStateManager manager, Graphics2D g, @NotNull Rectangle2D bounds, @NotNull Messages messages, Player player, boolean current, GameState state) {
        g.setColor(INFOBAR_BG);
        g.fill(bounds);
        g.setColor(INFOBAR_BORDER);
        g.draw(bounds);
        int rows = 3;
        int row = 0;
        {
            row = paintInfoBarRowInGame(manager, g, bounds, player.getName(), fontZX, INFOBAR_PLAYER, row, rows);
        }
        {
            int score = player.getScore();
            Color playerColor = score >= 0 ? INFOBAR_POSITIVE : INFOBAR_NEGATIVE;
            row = paintInfoBarRowInGame(manager, g, bounds, String.valueOf(score), fontLCD, playerColor, row, rows);
        }
        if (state.isInState(GameState.GAME)) {
            Color statusColor = current ? INFOBAR_PLAYER_MOVE : INFOBAR_PLAYER_WAIT;
            String statusLabel = current ? messages.get(MSG_INFOBAR_STATUS_MOVE) : messages.get(MSG_INFOBAR_STATUS_WAIT);
            row = paintInfoBarRowInGame(manager, g, bounds, statusLabel, fontZX, statusColor, row, rows);
        }
        assert row == rows;
    }

    private int paintInfoBarRowInGame(GraphicsStateManager manager, Graphics2D g, Rectangle2D bounds, @NotNull String nameLabel, Font font, Color color, int row, int rows) {
        manager.save();
        g.setFont(font);
        g.setColor(color);
        double barWidth = bounds.getWidth();
        double barHeight = bounds.getHeight() / rows;
        Rectangle2D.Double nameBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY() + barHeight * row, barWidth, barHeight);
        g.setClip(nameBounds);
        SwingDraw.updateFontSize(g, nameBounds, nameLabel);
        SwingDraw.drawStringCentered(g, nameBounds, nameLabel);
        manager.restore();
        return row + 1;
    }

    private void paintInfoInGame(@NotNull GraphicsStateManager manager, @NotNull Graphics2D g, Rectangle2D bounds, @NotNull Messages messages, @NotNull GameState state, List<Player> players, Player currentPlayer) {
        double barWidth = bounds.getWidth();
        double barHeight = bounds.getHeight() / players.size();
        for (int p = 0; p < players.size(); p++) {
            manager.save();
            Player player = players.get(p);
            boolean current = Objects.equals(player, currentPlayer);
            Rectangle2D.Double barBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY() + barHeight * p, barWidth, barHeight);
            g.setClip(barBounds);
            paintInfoBarInGame(manager, g, barBounds, messages, player, current, state);
            manager.restore();
        }
    }
}
