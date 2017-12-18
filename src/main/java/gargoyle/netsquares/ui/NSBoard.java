package gargoyle.netsquares.ui;

import gargoyle.netsquares.model.Action;
import gargoyle.netsquares.model.Board;
import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Game;
import gargoyle.netsquares.model.GameState;
import gargoyle.netsquares.model.KeyMap;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.Player;
import gargoyle.netsquares.model.player.HumanPlayer;
import gargoyle.netsquares.ui.painter.NSPainter;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class NSBoard extends JComponent {
    private Game game;
    private NSPainter painter;

    public NSBoard(NSPainter painter) {
        this.painter = painter;
        setOpaque(false);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                onKeyRelease(e.getKeyCode());
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (game.isState(GameState.GAME)) {
                    onMouseClick(e.getX(), e.getY(), painter);
                }
            }
        });
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public NSPainter getPainter() {
        return painter;
    }

    public void setPainter(NSPainter painter) {
        this.painter = painter;
    }

    private Rectangle2D.Double getRelBounds() {
        return new Rectangle2D.Double(0, 0, getWidth(), getHeight());
    }

    private void onKeyPress(int keyCode) {
        Player player = game.getCurrentPlayer();
        if (player instanceof HumanPlayer) {
            HumanPlayer humanPlayer = (HumanPlayer) player;
            KeyMap keyMap = humanPlayer.getKeyMap();
            Action action = keyMap.actionForKey(keyCode);
            if (action != null) {
                humanPlayer.onKeyPress(action);
            }
        }
    }

    private void onKeyRelease(int keyCode) {
        Player player = game.getCurrentPlayer();
        if (player instanceof HumanPlayer) {
            HumanPlayer humanPlayer = (HumanPlayer) player;
            KeyMap keyMap = humanPlayer.getKeyMap();
            Action action = keyMap.actionForKey(keyCode);
            if (action != null) {
                humanPlayer.onKeyRelease(action);
            }
        }
    }

    private void onMouseClick(int mouseX, int mouseY, NSPainter painter) {
        Player player = game.getCurrentPlayer();
        if (player instanceof HumanPlayer) {
            Board board = game.getBoard();
            Point cellAt = painter.getCellAt(getRelBounds(), board.getSize(), mouseX, mouseY);
            int caretX = board.getCaretX();
            int caretY = board.getCaretY();
            int x = cellAt.x;
            int y = cellAt.y;
            HumanPlayer humanPlayer = (HumanPlayer) player;
            if (x == caretX && y == caretY) {
                humanPlayer.onClick(new Action[]{Action.FIRE});
            } else {
                Directions directions = game.getAllowed(player);
                if (directions.isReachable(caretX, caretY, x, y)) {
                    Direction[] reachable = directions.getReachable(caretX, caretY, x, y);
                    int length = reachable == null ? 0 : reachable.length;
                    Action[] actions = new Action[length + 1];
                    for (int i = 0; i < length; i++) {
                        actions[i] = Action.forMove(Move.forDirection(reachable[i]));
                    }
                    actions[length] = Action.FIRE;
                    humanPlayer.onClick(actions.clone());
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game != null) {
            painter.paintBoard((Graphics2D) g, game, getRelBounds());
        }
    }
}
