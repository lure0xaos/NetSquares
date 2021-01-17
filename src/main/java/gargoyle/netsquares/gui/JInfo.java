package gargoyle.netsquares.gui;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.i.IPlayerInfo;
import gargoyle.netsquares.logic.i.IGame;
import gargoyle.netsquares.player.a.Player;

import javax.swing.*;
import java.awt.*;

public class JInfo extends JPanel implements IGameNotifier {
    private static final long serialVersionUID = 1L;
    private transient IGame game;

    public JInfo() {
        super();
        setLayout(new GridLayout(0, 1));
        setBackground(Color.BLACK);
    }

    protected IGame getGame() {
        return game;
    }

    private void setGame(final IGame game) {
        this.game = game;
    }

    @Override
    public GameNote notify(final GameNote note) {
        final IGame game = note.getGame();
        final GameNoteType type = note.getType();
        if (GameNoteType.NEW_GAME == type) {
            setGame(game);
            for (final Player player : this.game.getPlayers()) {
                final JPlayerInfo playerInfo = new JPlayerInfo(player);
                playerInfo.updateInfo(game);
                add(playerInfo);
            }
        }
        if (GameNoteType.GAME_OVER == type) {
            for (int i = 0; i < getComponentCount(); i++) {
                final Component c = getComponent(i);
                if (c instanceof IPlayerInfo) {
                    remove(c);
                }
            }
        }
        if (GameNoteType.PLAYER_MOVED == type) {
            for (int i = 0; i < getComponentCount(); i++) {
                final Component c = getComponent(i);
                if (c instanceof IPlayerInfo) {
                    final IPlayerInfo playerInfo = (IPlayerInfo) c;
                    playerInfo.updateInfo(game);
                }
            }
            repaint();
        }
        if (GameNoteType.PLAYER_MOVING == type) {
            repaint();
        }
        return null;
    }
}
