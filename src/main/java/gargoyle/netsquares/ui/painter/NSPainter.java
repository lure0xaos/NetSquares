package gargoyle.netsquares.ui.painter;

import gargoyle.netsquares.model.Board;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Game;
import gargoyle.netsquares.model.GameState;
import gargoyle.netsquares.model.Player;
import gargoyle.netsquares.res.Messages;
import gargoyle.netsquares.res.NSContext;
import gargoyle.netsquares.ui.painter.state.GraphicsStateManager;
import gargoyle.netsquares.util.InterpolUtils;
import gargoyle.netsquares.util.SwingDraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Objects;

public class NSPainter {
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
    private final Image background;
    private final Font fontLCD;
    private final Font fontZX;

    public NSPainter(NSContext context, String background, String backgroundSuffix) {
        fontLCD = context.loadFont(RES_FONT_LCD);
        fontZX = context.loadFont(RES_FONT_ZX);
        this.background = context.loadImage(background, backgroundSuffix);
    }

    public Point getCellAt(Rectangle2D bounds, int boardSize, double x, double y) {
        return new Point((int) ((x - bounds.getX()) * boardSize / bounds.getWidth()), (int) ((y - bounds.getY()) * boardSize / bounds.getHeight()));
    }

    public void paintBoard(Graphics2D g, Game game, Rectangle2D bounds) {
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

    private void paintBoardInGame(GraphicsStateManager manager, Graphics2D g, Rectangle2D bounds, Board board, Directions allowed) {
        g.setColor(CELL_BORDER);
        g.fill(bounds);
        paintBackground(g, background, bounds);
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

    private void paintBackground(Graphics2D g, Image background, Rectangle2D bounds) {
        Rectangle2D inner = new Rectangle2D.Double(0, 0,
                background.getWidth(null), background.getHeight(null));
        Rectangle2D.Double fit = new Rectangle2D.Double();
        if (bounds.getWidth() > bounds.getHeight()) {
            fit.height = bounds.getHeight();
            fit.width = inner.getHeight() / inner.getWidth() * bounds.getHeight();
        } else {
            fit.width = bounds.getWidth();
            fit.height = inner.getHeight() / inner.getWidth() * bounds.getWidth();
        }
        fit.x = bounds.getX() + (bounds.getWidth() - fit.getWidth()) / 2;
        fit.y = bounds.getY() + (bounds.getHeight() - fit.getHeight()) / 2;
        g.drawImage(background, (int) fit.getX(), (int) fit.getY(),
                (int) (fit.getX() + fit.getWidth()), (int) (fit.getY() + fit.getHeight()),
                0, 0, background.getWidth(null), background.getHeight(null), null);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private void paintBoardInOpen(GraphicsStateManager manager, Graphics2D g, Game game, Rectangle2D bounds) {
        g.setColor(CELL_BORDER);
        g.fill(bounds);
        paintBackground(g, background, bounds);
    }

    public Rectangle2D.Double getCellBounds(Rectangle2D bounds, int boardSize, int x, int y) {
        double cellWidth = bounds.getWidth() / boardSize;
        double cellHeight = bounds.getHeight() / boardSize;
        return new Rectangle2D.Double(bounds.getX() + x * cellWidth, bounds.getY() + y * cellHeight, cellWidth, cellHeight);
    }

    private void paintCellInGame(Graphics2D g, boolean open, int score, boolean caret, boolean reachable, Rectangle2D bounds) {
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

    private void paintCellCaretInGame(Graphics2D g, Rectangle2D bounds) {
        paintCellBorderInGame(g, bounds, CELL_CARET);
    }

    private void paintCellBorderInGame(Graphics2D g, Rectangle2D bounds, Color color) {
        int range = InterpolUtils.toRange((int) (bounds.getWidth() / 10), CARET_WIDTH_MIN, CARET_WIDTH_MAX);
        for (int i = 0; i < range; i++) {
            g.setColor(InterpolUtils.interpolate(CELL_BORDER, color, range, i));
            g.draw(new Rectangle2D.Double(bounds.getX() + i, bounds.getY() + i, bounds.getWidth() - i - i, bounds.getHeight() - i - i));
        }
    }

    public void paintInfo(Graphics2D g, Game game, Rectangle2D bounds, Messages messages) {
        try (GraphicsStateManager manager = new GraphicsStateManager(g, true)) {
            manager.save();
            GameState state = game.getState();
            if (game.isState(GameState.GAME, GameState.OPEN)) {
                paintInfoInGame(manager, g, bounds, messages, state, game.getPlayers(), game.getCurrentPlayer());
            }
            manager.restore();
        }
    }

    private void paintInfoInGame(GraphicsStateManager manager, Graphics2D g, Rectangle2D bounds, Messages messages, GameState state, List<Player> players, Player currentPlayer) {
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

    @SuppressWarnings("UnnecessaryCodeBlock")
    private void paintInfoBarInGame(GraphicsStateManager manager, Graphics2D g, Rectangle2D bounds, Messages messages, Player player, boolean current, GameState state) {
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

    private int paintInfoBarRowInGame(GraphicsStateManager manager, Graphics2D g, Rectangle2D bounds, String nameLabel, Font font, Color color, int row, int rows) {
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
}
