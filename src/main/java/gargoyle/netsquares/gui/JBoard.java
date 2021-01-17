package gargoyle.netsquares.gui;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.i.Adaptable;
import gargoyle.netsquares.gui.util.AdaptHelper;
import gargoyle.netsquares.logic.i.IGame;
import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.i.IBoardView;
import gargoyle.netsquares.player.HumanPlayer;

import javax.swing.*;
import java.awt.*;

public class JBoard extends JComponent implements IGameNotifier, Adaptable {
    private static final long serialVersionUID = 1L;
    private static final int initialCaretSize = 5;
    private int caretSize;
    private transient IGame game;

    public JBoard() {
        AdaptHelper.addAdaptListeners(this);
    }

    @Override
    public void adapt() {
        adjustFont();
        adjustCaret();
    }

    void adjustCaret() {
        if ((game == null) || (game.getBoard() == null)) {
            caretSize = initialCaretSize;
        } else {
            final Rectangle cellRectangle = getCellRectangle(0, 0);
            caretSize = Math.max(5, cellRectangle.height / 10);
        }
    }

    void adjustFont() {
        if (game != null) {
            if (isVisible()) {
                final IBoardView board = game.getBoard();
                if (board != null) {
                    adjustFont(String.valueOf(board.getScore()), 0);
                }
            }
        }
    }

    private void adjustFont(final String largest, final int grow) {
        final Rectangle cell = getCellRectangle(0, 0);
        Font font = getFont();
        while (true) {
            final FontMetrics fm = getFontMetrics(font);
            final Rectangle bounds = fm.getStringBounds(largest, getGraphics()).getBounds();
            if ((bounds.width > cell.width) || (bounds.height > cell.height)) {
                if (grow == 0) {
                    adjustFont(largest, -1);
                    break;
                }
                if (grow > 0) {
                    setFont(font);
                    break;
                }
                if (grow < 0) {
                    font = font.deriveFont((float) (font.getSize() + grow));
                    continue;
                }
            }
            if ((bounds.width < cell.width) || (bounds.height < cell.height)) {
                if (grow == 0) {
                    adjustFont(largest, 1);
                    break;
                }
                if (grow > 0) {
                    font = font.deriveFont((float) (font.getSize() + grow));
                    continue;
                }
                if (grow < 0) {
                    setFont(font);
                    break;
                }
            }
        }
    }

    private Color getCaretColor(final int score, final int k) {
        final int a = (255 * k) / caretSize;
        final Color c;
        if (score == 0) {
            c = new Color(a, a, a);
        } else {
            c = score > 0 ? new Color(0, a, 0) : new Color(a, 0, 0);
        }
        return c;
    }

    private Rectangle getCellRectangle(final int x, final int y) {
        return getRectangle(new Rectangle(0, 0, getWidth(), getHeight()), new Point(x, y));
    }

    protected IGame getGame() {
        return game;
    }

    private Rectangle getRectangle(final Rectangle whole, final Point p) {
        final Rectangle rectangle = new Rectangle();
        final IBoardView board = game.getBoard();
        final int boardSize = board.getBoardSize();
        final double width = boardSize == 0 ? whole.width : whole.width / (double) boardSize;
        final double height = boardSize == 0 ? whole.height : whole.height / (double) boardSize;
        rectangle.width = (int) (width);
        rectangle.height = (int) (height);
        rectangle.x = (int) (p.x * width);
        rectangle.y = (int) (p.y * height);
        return rectangle;
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public GameNote notify(final GameNote note) {
        final GameNoteType type = note.getType();
        if (GameNoteType.NEW_GAME == type) {
            if (game == null) {
                game = note.getGame();
                changeCursor();
                return new GameNote(note.getGame(), GameNoteType.GAME_COVER);
            }
            changeCursor();
        }
        if (GameNoteType.GAME_COVER == type) {
            if (isVisible() && adaptCover()) {
                adjustCaret();
                changeCursor();
                repaint();
                return note;
            }
        }
        if (GameNoteType.GAME_OVER == type) {
            setCursorNormal();
            repaint();
        }
        if (GameNoteType.PLAYER_MOVED == type) {
            changeCursor();
            repaint();
        }
        if (GameNoteType.PLAYER_MOVING == type) {
            changeCursor();
            repaint();
        }
        return null;
    }

    private boolean adaptCover() {
        if (game != null) {
            final Image cover = game.getCover();
            if (cover != null) {
                final int width = cover.getWidth(this);
                final int height = cover.getHeight(this);
                final Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
                final int size = Math.min(Math.min(bounds.width, bounds.height - 40), Math.max(width, height));
                final Dimension preferredSize = new Dimension(size, size);
                if (!preferredSize.equals(getPreferredSize())) {
                    setPreferredSize(preferredSize);
                    setMinimumSize(preferredSize);
                    setMaximumSize(preferredSize);
                    setSize(preferredSize);
                    return true;
                }
            }
        }
        return false;
    }

    private void changeCursor() {
        if (game.getCurrentPlayer() == null || !(game.getCurrentPlayer() instanceof HumanPlayer)) {
            setCursorWait();
        } else {
            setCursorNormal();
        }
    }

    private void setCursorWait() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    private void setCursorNormal() {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        paintBoard(g);
    }

    private void paintBgCell(final Graphics g, final boolean caret, final int score, final Rectangle cellRect) {
        final Color color = g.getColor();
        g.setColor(game.getColor(score));
        paintBorder(g, score, cellRect);
        g.setColor(game.getColor(score));
        g.fill3DRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
        g.setColor(color);
        g.drawRect(cellRect.x, cellRect.y, cellRect.width, cellRect.height);
        if (caret) {
            paintCaret(game.getCurrentPlayer().getAllowedDirections(), g, score, cellRect);
        }
        g.setColor(Color.BLACK);
        final String text = String.valueOf(score < 0 ? -score : score);
        final FontMetrics fm = getFontMetrics(getFont());
        final Rectangle bounds = fm.getStringBounds(text, g).getBounds();
        g.drawString(text, cellRect.x + ((cellRect.width - bounds.width) / 2),
                cellRect.y + ((cellRect.height - bounds.height) / 2) + fm.getAscent());
        g.setColor(color);
    }

    private void paintBoard(final Graphics g) {
        if ((game == null)) {
            return;
        }
        final IBoardView board = game.getBoard();
        if (board == null) {
            return;
        }
        if (board.isOpen()) {
            final Image cover = game.getCover();
            g.drawImage(cover, 0, 0, getWidth(), getHeight(), 0, 0, cover.getWidth(this), cover.getHeight(this), this);
        }
        final Color c = g.getColor();
        final int size = board.getBoardSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                final int score = board.getCellAt(x, y);
                final Rectangle cellRect = getCellRectangle(x, y);
                final Point caret = board.getCaret();
                final boolean isCaret = (caret != null) && (caret.x == x) && (caret.y == y);
                paintCell(game, g, x, y, isCaret, score, cellRect);
            }
        }
        g.setColor(c);
    }

    private void paintBorder(final Graphics g, final int score, final Rectangle rectangle) {
        final Color c = g.getColor();
        for (int k = caretSize; k > 0; k--) {
            g.setColor(getCaretColor(score, k));
            g.drawRect(rectangle.x + k, rectangle.y + k, rectangle.width - k - k, rectangle.height - k - k);
        }
        g.setColor(c);
    }

    private void paintCaret(final Directions allowedDirections, final Graphics g, final int score,
                            final Rectangle rectangle) {
        paintBorder(g, score, rectangle);
        final Color c = g.getColor();
        for (int k = caretSize; k > 0; k--) {
            g.setColor(getCaretColor(score, k));
            if (allowedDirections.contains(Direction.UP)) {
                final int x1 = ((rectangle.x + (rectangle.width / 2)) - (caretSize / 2)) + k;
                final int y1 = (rectangle.y + caretSize) - k;
                final int x2 = (rectangle.x + (rectangle.width / 2) + (caretSize / 2)) - k;
                final int y2 = (rectangle.y + caretSize) - k;
                final int x3 = rectangle.x + (rectangle.width / 2);
                final int y3 = rectangle.y;
                g.drawLine(x1, y1, x2, y2);
                g.drawLine(x2, y2, x3, y3);
                g.drawLine(x3, y3, x1, y1);
            }
            if (allowedDirections.contains(Direction.DOWN)) {
                final int x1 = ((rectangle.x + (rectangle.width / 2)) - (caretSize / 2)) + k;
                final int y1 = ((rectangle.y + rectangle.height) - caretSize) + k;
                final int x2 = (rectangle.x + (rectangle.width / 2) + (caretSize / 2)) - k;
                final int y2 = ((rectangle.y + rectangle.height) - caretSize) + k;
                final int x3 = rectangle.x + (rectangle.width / 2);
                final int y3 = rectangle.y + rectangle.height;
                g.drawLine(x1, y1, x2, y2);
                g.drawLine(x2, y2, x3, y3);
                g.drawLine(x3, y3, x1, y1);
            }
            if (allowedDirections.contains(Direction.LEFT)) {
                final int x1 = (rectangle.x + caretSize) - k;
                final int y1 = ((rectangle.y + (rectangle.height / 2)) - (caretSize / 2)) + k;
                final int x2 = (rectangle.x + caretSize) - k;
                final int y2 = (rectangle.y + (rectangle.height / 2) + (caretSize / 2)) - k;
                final int x3 = rectangle.x;
                final int y3 = rectangle.y + (rectangle.height / 2);
                g.drawLine(x1, y1, x2, y2);
                g.drawLine(x2, y2, x3, y3);
                g.drawLine(x3, y3, x1, y1);
            }
            if (allowedDirections.contains(Direction.RIGHT)) {
                final int x1 = ((rectangle.x + rectangle.width) - caretSize) + k;
                final int y1 = ((rectangle.y + (rectangle.height / 2)) - (caretSize / 2)) + k;
                final int x2 = ((rectangle.x + rectangle.width) - caretSize) + k;
                final int y2 = (rectangle.y + (rectangle.height / 2) + (caretSize / 2)) - k;
                final int x3 = rectangle.x + rectangle.width;
                final int y3 = rectangle.y + (rectangle.height / 2);
                g.drawLine(x1, y1, x2, y2);
                g.drawLine(x2, y2, x3, y3);
                g.drawLine(x3, y3, x1, y1);
            }
        }
        g.setColor(c);
    }

    private void paintCell(final IGame game, final Graphics g, final int x, final int y, final boolean caret,
                           final int score, final Rectangle cellRect) {
        final Color c = g.getColor();
        if (score == 0) {
            paintImageCell(g, x, y, cellRect);
            if (caret) {
                paintCaret(game.getCurrentPlayer().getAllowedDirections(), g, score, cellRect);
            }
        } else {
            paintBgCell(g, caret, score, cellRect);
        }
        g.setColor(c);
    }

    private void paintImageCell(final Graphics g, final Image image, final int x, final int y,
                                final Rectangle cellRect) {
        if (image == null) {
            return;
        }
        final Rectangle imgCellRect = getRectangle(new Rectangle(0, 0, image.getWidth(this), image.getHeight(this)), new Point(x, y));
        g.drawImage(image, cellRect.x, cellRect.y, cellRect.x + cellRect.width, cellRect.y + cellRect.height,
                imgCellRect.x, imgCellRect.y, imgCellRect.x + imgCellRect.width, imgCellRect.y + imgCellRect.height,
                this);
    }

    private void paintImageCell(final Graphics g, final int x, final int y, final Rectangle cellRect) {
        paintImageCell(g, game.getCover(), x, y, cellRect);
    }

}
