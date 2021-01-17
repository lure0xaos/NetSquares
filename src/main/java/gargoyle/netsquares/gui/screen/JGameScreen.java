package gargoyle.netsquares.gui.screen;

import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.JBoard;
import gargoyle.netsquares.gui.JInfo;
import gargoyle.netsquares.gui.a.JScreenPanel;
import gargoyle.netsquares.gui.i.Adaptable;
import gargoyle.netsquares.gui.util.AdaptHelper;

import java.awt.*;

public class JGameScreen extends JScreenPanel implements Adaptable {
    private static final long serialVersionUID = 1L;
    private static final String GAME_SCREEN = "gameScreen";

    public JGameScreen(IGameNotifier notifier) {
        super(notifier, GAME_SCREEN, new BorderLayout());
        AdaptHelper.addAdaptListeners(this);
        add(new JBoard(), BorderLayout.CENTER);
        add(new JInfo(), BorderLayout.EAST);
    }

    @Override
    public void adapt() {
        invalidate();
    }

    @Override
    public GameNote notify(GameNote note) {
        if (GameNoteType.SCREEN_SHOWN == note.getType()) {
            super.notify(new GameNote(note.getGame(),GameNoteType.NEW_GAME));
        }
        return null;
    }
}
