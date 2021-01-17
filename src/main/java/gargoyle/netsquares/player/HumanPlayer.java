package gargoyle.netsquares.player;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.model.Direction;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.model.Move;
import gargoyle.netsquares.model.i.IBoardView;
import gargoyle.netsquares.player.a.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HumanPlayer extends Player implements KeyListener {
    private transient volatile int key;
    private volatile boolean stopped;

    public HumanPlayer(final String name, final Directions allowedDirections) {
        super(name, allowedDirections);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        //
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        if (key == 0) {
            final int keyCode = e.getKeyCode();
            if ((keyCode == KeyEvent.VK_UP) || (keyCode == KeyEvent.VK_DOWN) || (keyCode == KeyEvent.VK_LEFT)
                    || (keyCode == KeyEvent.VK_RIGHT) || (keyCode == KeyEvent.VK_ENTER)
                    || (keyCode == KeyEvent.VK_ESCAPE)) {
                key = keyCode;
            }
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        //
    }

    @Override
    public Move move(final IBoardView board) {
        stopped = false;
        while (!stopped) {
            if (key == 0) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    key = 0;
                    return null;
                }
            }
            if (key == KeyEvent.VK_ESCAPE) {
                key = 0;
                return null;
            }
            if (key == KeyEvent.VK_ENTER) {
                key = 0;
                return new Move(null, 0);
            }
            if (key == KeyEvent.VK_UP) {
                final Move move = board.getNearestMove(Direction.UP);
                key = 0;
                if (move != null) {
                    return move;
                }
            }
            if (key == KeyEvent.VK_DOWN) {
                final Move move = board.getNearestMove(Direction.DOWN);
                key = 0;
                if (move != null) {
                    return move;
                }
            }
            if (key == KeyEvent.VK_LEFT) {
                final Move move = board.getNearestMove(Direction.LEFT);
                key = 0;
                if (move != null) {
                    return move;
                }
            }
            if (key == KeyEvent.VK_RIGHT) {
                final Move move = board.getNearestMove(Direction.RIGHT);
                key = 0;
                if (move != null) {
                    return move;
                }
            }
        }
        return null;
    }

    @Override
    public GameNote notify(final GameNote note) {
        if (GameNoteType.GAME_OVER == note.getType()) {
            stopped = true;
        }
        return null;
    }
}
