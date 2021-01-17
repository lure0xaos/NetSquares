package gargoyle.netsquares;

import gargoyle.netsquares.beans.Messages;
import gargoyle.netsquares.beans.Resources;
import gargoyle.netsquares.events.GameNote;
import gargoyle.netsquares.events.GameNoteType;
import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.gui.a.JScreenPanel;
import gargoyle.netsquares.gui.i.Adaptable;
import gargoyle.netsquares.gui.i.NSScreen;
import gargoyle.netsquares.gui.screen.JGameScreen;
import gargoyle.netsquares.gui.screen.JMenuScreen;
import gargoyle.netsquares.gui.util.AdaptHelper;
import gargoyle.netsquares.i.INetSquares;
import gargoyle.netsquares.logic.Game;
import gargoyle.netsquares.logic.i.IGame;
import gargoyle.netsquares.model.Directions;
import gargoyle.netsquares.player.ComputerPlayer;
import gargoyle.netsquares.player.HumanPlayer;
import gargoyle.netsquares.player.a.Player;
import gargoyle.netsquares.util.Images;
import gargoyle.netsquares.util.Randoms;
import gargoyle.netsquares.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NetSquares extends JFrame implements IGameNotifier, INetSquares, Adaptable {
    private static final String MSG_EXIT = "exit";
    private static final String MSG_PLAYER = "player";
    private static final String MSG_WIN = "win";
    private static final String MSG_DRAW = "draw";
    private static final String ICON_LOCATION = "gargoyle/netsquares/ns.gif";
    private static final int ICON_SIZE = 32;
    private static final long serialVersionUID = 1L;
    private static final Image iconImage;
    private static final Messages messages;

    static {
        try {
            iconImage = Images.fitImage(Resources.forCurrentThread().loadImage(ICON_LOCATION), new Rectangle(ICON_SIZE, ICON_SIZE));
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            messages = Messages.messages(NetSquares.class);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private transient IGame game;
    private CardLayout layout;
    private JGameScreen gameScreen;
    private JMenuScreen menuScreen;
    private NSScreen currentScreen;
    private volatile boolean disposing;

    public NetSquares() {
        super();
        init();
    }

    public static void main(final String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new NetSquares().menu();
    }

    @Override
    public void alert(final String message) {
        JOptionPane.showMessageDialog(this, message, getTitle(), JOptionPane.WARNING_MESSAGE,
                new ImageIcon(getIconImage()));
    }

    @Override
    public boolean confirm(final String message) {
        return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, message, getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(getIconImage()));
    }

    @Override
    public Player createComputerPlayer(final String name, final Directions allowedDirections) {
        final ComputerPlayer player = new ComputerPlayer(name, allowedDirections);
        game.addGameNotifier(player);
        return player;
    }

    @Override
    public Player createHumanPlayer(final String name, final Directions allowedDirections) {
        final HumanPlayer player = new HumanPlayer(name, allowedDirections);
        addKeyListener(player);
        game.addGameNotifier(player);
        return player;
    }

    private void destroy() {
        if (game != null) {
            if (game.isInGame()) {
                game.stopGame();
            }
            game.removeGameNotifiers();
        }
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            if (c instanceof NSScreen) {
                ((NSScreen) c).unbind(game);
            }
        }
        AdaptHelper.removeAdaptListeners(this);
//        getContentPane().removeAll();
        setVisible(false);
        disposing = true;

    }

    private void gameOver(final List<Player> winners) {
        final List<String> winnersList = new ArrayList<String>(winners.size());
        for (final Player player : winners) {
            winnersList.add(messages.message(MSG_PLAYER, player.getName(), player.getClass().getSimpleName(), player.getScore()));
        }
        alert(winners.size() == 1 ? messages.message(MSG_WIN, Strings.join(", ", winnersList)) : messages.message(MSG_DRAW));
        if (disposing) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    dispose();
                }
            });
        } else {
            if (isVisible()) showScreen(menuScreen);
        }
    }

    private void init() {
        disposing = false;
        {
            game = new Game();
            game.addGameNotifier(this);
        }
        {
            setTitle(getClass().getSimpleName());

            setIconImage(iconImage);
            setLayout(layout = new CardLayout());
        }
        {

        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                exit();
            }
        });
    }

    void exit() {
        final boolean ask = game != null && game.isInGame();
        if (ask) {
            if (confirm(messages.message(MSG_EXIT))) {
                destroy();
            }
        } else {
            destroy();
        }
    }

    @Override
    public GameNote notify(final GameNote note) {
        if (GameNoteType.GAME_COVER == note.getType()) {
            setPreferredSize(null);
//            setVisible(false);
            pack();
            setLocationRelativeTo(null);
//            setVisible(true);
        }
        if (GameNoteType.NEW_GAME == note.getType()) {
            if (note.getValue() != null)
                try {
                    squares();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public void showScreen(final NSScreen screen) {
        if (currentScreen != null) {
            currentScreen.notify(new GameNote<NSScreen>(game, GameNoteType.SCREEN_HIDDEN, currentScreen));
            ((Component) currentScreen).setVisible(false);
        }
        present();
        currentScreen = screen;
        layout.show(getContentPane(), screen.getName());
        ((Component) screen).setVisible(true);
        ((Component) screen).requestFocus();
        screen.notify(new GameNote<NSScreen>(game, GameNoteType.SCREEN_SHOWN, screen));
        ((Component) screen).repaint();
    }

    private void present() {
        if (!isVisible()) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        setVisible(true);
                        setAlwaysOnTop(true);
                        toFront();
                        setAlwaysOnTop(false);

                    }
                });
            } catch (final InterruptedException e) {
                e.printStackTrace();
            } catch (final InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void addScreen(JScreenPanel screen) {
        screen.bind(game);
        getContentPane().add(screen, screen.getName());
        layout.addLayoutComponent(screen, screen.getName());
    }

    private void squares() throws FileNotFoundException {
        if (gameScreen == null) {
            gameScreen = new JGameScreen(this);
            addScreen(gameScreen);
        }
        game.setBoard(8, 11);
        showScreen(gameScreen);
        final String cover = "UncoverMeIII.jpg";
//        final String cover = "img.jpg";
        try {
            game.setCover(cover);
        } catch (final IOException e) {
            throw new FileNotFoundException("cannot load cover image: " + cover);
        }
        final List<Player> players = new ArrayList<Player>();
        final boolean b = Randoms.random(0, 2) != 0;
        players.add(createHumanPlayer("Me", b ? Directions.HORIZONTAL : Directions.VERTICAL));
        players.add(createComputerPlayer("Comp", b ? Directions.VERTICAL : Directions.HORIZONTAL));
        setFocusable(true);
        requestFocusInWindow();
        new Thread() {
            @Override
            public void run() {
                gameOver(game.game(players));
            }
        }.start();
    }

    private void menu() {
        if (menuScreen == null) {
            menuScreen = new JMenuScreen(this);
            addScreen(menuScreen);
        }
        final Dimension size = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
        setPreferredSize(new Dimension(size.width / 2, size.height / 2 - 40));
        pack();
        setLocationRelativeTo(null);
        showScreen(menuScreen);
    }

    @Override
    public void adapt() {
        //
    }
}
