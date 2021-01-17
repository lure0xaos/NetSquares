package gargoyle.netsquares.gui.screen;

import gargoyle.netsquares.beans.Messages;
import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.a.JScreenPanel;
import gargoyle.netsquares.gui.i.Adaptable;
import gargoyle.netsquares.gui.util.AdaptHelper;
import gargoyle.netsquares.logic.i.IGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JMenuScreen extends JScreenPanel implements Adaptable {
    private static final long serialVersionUID = 1L;
    private static final String MENU_SCREEN = "menuScreen";
    private static final Messages messages;
    private static final String ITEM_HUMAN_VS_COMPUTER = "human_vs_computer";
    public static final String ITEM_COMPUTER_VS_COMPUTER = "computer_vs_computer";
    public static final String ITEM_HUMAN_VS_HUMAN = "human_vs_human";
    public static final String ITEM_HUMAN_VS_HUMAN_REMOTE = "human_vs_human_remote";

    static {
        try {
            messages = Messages.messages(JMenuScreen.class);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public JMenuScreen(final IGameNotifier notifier) {
        super(notifier, MENU_SCREEN, new GridLayout(0, 1));
        makeAdaptable();
        createMenuItem(notifier, messages.message(ITEM_HUMAN_VS_COMPUTER), GameNoteType.NEW_GAME, ITEM_HUMAN_VS_COMPUTER);
        createMenuItem(notifier, messages.message(ITEM_COMPUTER_VS_COMPUTER), GameNoteType.NEW_GAME, ITEM_COMPUTER_VS_COMPUTER);
    }

    private void makeAdaptable() {
        AdaptHelper.addAdaptListeners(this);
    }

    @Override
    public void adapt() {
        int n = 0;
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof MenuButton) n++;
        }
        final int hGap = getWidth() / 5;
        final int vGap = getHeight() / n / 10;
        GridLayout layout = (GridLayout) getLayout();
        layout.setHgap(hGap);
        layout.setVgap(vGap);
        setBorder(BorderFactory.createEmptyBorder(vGap, hGap, vGap, hGap));
        revalidate();
    }

    private void createMenuItem(final IGameNotifier notifier, final String message, final GameNoteType type, final String item) {
        add(new MenuButton(message, notifier, type, item));
    }


    private static class MenuButton extends JButton implements IGameNotifier {
        private transient IGame game;

        public MenuButton(String message, final IGameNotifier notifier, final GameNoteType type, final String item) {
            super(message);
            setAction(new AbstractAction(message) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    notifier.notify(new GameNote<String>(game, type, item));
                }
            });
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

        @Override
        public GameNote notify(GameNote note) {
            if (GameNoteType.SCREEN_SHOWN == note.getType()) {
                game = note.getGame();
            }
            return null;
        }
    }
}
