package gargoyle.netsquares.ui;

import gargoyle.netsquares.i18n.Messages;
import gargoyle.netsquares.model.Game;
import gargoyle.netsquares.model.GameState;
import gargoyle.netsquares.model.Player;
import gargoyle.netsquares.model.player.DumbPlayer;
import gargoyle.netsquares.model.player.HumanPlayer;
import gargoyle.netsquares.prop.base.BaseRWProperty;
import gargoyle.netsquares.prop.base.IROProperty;
import gargoyle.netsquares.prop.base.IRWProperty;
import gargoyle.netsquares.res.Resources;
import gargoyle.netsquares.store.Options;
import gargoyle.netsquares.ui.painter.NSPainter;
import gargoyle.netsquares.util.SwingDraw;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NSFrame extends JFrame {
    private static final int BOARD_SIZE = 10;
    private static final int BOARD_WEIGHT = 11;
    private static final String MSG_DRAW = "draw";
    private static final String MSG_MENU_GAME = "menu.game";
    private static final String MSG_MENU_GAME_CVC = "menu.game.cvc";
    private static final String MSG_MENU_GAME_HVC = "menu.game.hvc";
    private static final String MSG_MENU_GAME_HVH = "menu.game.hvh";
    private static final String MSG_MENU_OPTIONS = "menu.options";
    private static final String MSG_MENU_OPTIONS_MUSIC = "menu.options.music";
    private static final String MSG_MENU_OPTIONS_PLAYER_1 = "menu.options.player.1";
    private static final String MSG_MENU_OPTIONS_PLAYER_2 = "menu.options.player.2";
    private static final String MSG_MENU_OPTIONS_SOUND = "menu.options.sound";
    private static final String MSG_PLAYER_1 = "player.1";
    private static final String MSG_PLAYER_2 = "player.2";
    private static final String MSG_PLAYER_COMPUTER = "player.computer";
    private static final String MSG_TITLE = "title";
    private static final String MSG__WINNER_S = "winner_s";
    private static final String RES_ICON = "icon.png";
    private final @NotNull Game game;
    private final @NotNull Messages messages;
    private NSBoard board;
    private NSInfo info;
    private final ScheduledExecutorService service;

    @SuppressWarnings("MagicNumber")
    public NSFrame(@NotNull Messages messages, NSPainter painter, Options options) throws HeadlessException {
        this.messages = messages;
        game = new Game(options);
        init(getContentPane(), painter);
        setJMenuBar(initMenuBar(messages, options));
        board.setGame(game);
        info.setGame(game);
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            if (game.isState(GameState.GAME) && !game.move()) {
                onGameOver(game, game.computeWinner());
            }
            repaint();
        }, 1000, 300, TimeUnit.MILLISECONDS);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                board.requestFocusInWindow();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                destroy(service);
            }
        });
    }

    private void destroy(ExecutorService service) {
        service.shutdown();
        dispose();
    }

    private void doBegin() {
        board.requestFocusInWindow();
    }

    private void doEnd() {
    }

    private void init(Container pane, NSPainter painter) {
        setTitle(messages.get(MSG_TITLE));
        setIconImage(Resources.loadImage(RES_ICON, Locale.getDefault()));
        pane.setLayout(new BorderLayout());
        board = new NSBoard(painter);
        pane.add(board, BorderLayout.CENTER);
        info = new NSInfo(messages, painter);
        pane.add(info, BorderLayout.EAST);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = getSize();
                int min = Math.min(size.width, size.height);
                Dimension boardSize = new Dimension(min, min);
                board.setPreferredSize(boardSize);
                board.setMinimumSize(boardSize);
                board.setMaximumSize(boardSize);
                Dimension infoSize = new Dimension(size.width - min, min);
                info.setPreferredSize(infoSize);
                info.setMinimumSize(infoSize);
                info.setMaximumSize(infoSize);
                revalidate();
            }
        });
        Rectangle2D bounds = SwingDraw.getMaximumWindowBounds();
        pane.setPreferredSize(new Dimension((int) bounds.getWidth() / 2, (int) bounds.getHeight() / 2));
        pack();
        setLocationRelativeTo(null);
    }

    private void ask(IRWProperty<String> property, String message) {
        String initialSelectionValue = property.get();
        String value = JOptionPane.showInputDialog(NSFrame.this, message, initialSelectionValue);
        property.set(value);
    }

    private @NotNull JMenuBar initMenuBar(@NotNull Messages messages, Options options) {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(initGameMenu(messages, options));
        menuBar.add(initOptionsMenu(messages, options));
        return menuBar;
    }

    private @NotNull JMenu initGameMenu(Messages messages, Options options) {
        JMenu menu = new JMenu(messages.get(MSG_MENU_GAME));
        menu.add(new JMenuItem(new AbstractAction(messages.get(MSG_MENU_GAME_HVC)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = options.player1().get();
                onNewGame(new HumanPlayer(name), new DumbPlayer(messages.get(MSG_PLAYER_COMPUTER)));
            }
        }));
        menu.add(new JMenuItem(new AbstractAction(messages.get(MSG_MENU_GAME_HVH)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name1 = options.player1().get();
                String name2 = options.player2().get();
                onNewGame(new HumanPlayer(name1), new HumanPlayer(name2));
            }
        }));
        menu.add(new JMenuItem(new AbstractAction(messages.get(MSG_MENU_GAME_CVC)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNewGame(new DumbPlayer(messages.get(MSG_PLAYER_1)), new DumbPlayer(messages.get(MSG_PLAYER_2)));
            }
        }));
        return menu;
    }

    private @NotNull JMenu initOptionsMenu(Messages messages, Options options) {
        JMenu menu = new JMenu(messages.get(MSG_MENU_OPTIONS));
        BaseRWProperty<Boolean> soundProperty = options.sound();
        BaseRWProperty<Boolean> musicProperty = options.music();
        BaseRWProperty<String> player1Property = options.player1();
        BaseRWProperty<String> player2Property = options.player2();
        String nameSound = messages.get(MSG_MENU_OPTIONS_SOUND);
        JCheckBoxMenuItem itemSound = new JCheckBoxMenuItem(new AbstractAction(nameSound) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean soundSelected = ((AbstractButton) e.getSource()).isSelected();
                soundProperty.set(soundSelected);
                toggleMusic(musicProperty, soundProperty);
            }
        });
        Boolean sound = soundProperty.get();
        itemSound.setSelected(sound);
        menu.add(itemSound);
        String nameMusic = messages.get(MSG_MENU_OPTIONS_MUSIC);
        JCheckBoxMenuItem itemMusic = new JCheckBoxMenuItem(new AbstractAction(nameMusic) {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean musicSelected = ((AbstractButton) e.getSource()).isSelected();
                musicProperty.set(musicSelected);
                toggleMusic(musicProperty, soundProperty);
            }
        });
        Boolean music = musicProperty.get();
        itemMusic.setSelected(music);
        menu.add(itemMusic);
        menu.add(new JMenuItem(new AbstractAction(messages.get(MSG_MENU_OPTIONS_PLAYER_1)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseRWProperty<String> property = player1Property;
                String message = messages.get(MSG_MENU_OPTIONS_PLAYER_1);
                ask(property, message);
            }
        }));
        menu.add(new JMenuItem(new AbstractAction(messages.get(MSG_MENU_OPTIONS_PLAYER_2)) {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseRWProperty<String> property = player2Property;
                String message = messages.get(MSG_MENU_OPTIONS_PLAYER_2);
                ask(property, message);
            }
        }));
        return menu;
    }

    private void toggleMusic(IROProperty<Boolean> musicProperty, IROProperty<Boolean> soundProperty) {
        Boolean music = musicProperty.get();
        Boolean sound = soundProperty.get();
        if (!sound || !music) {
            game.musicStop();
        }
        if (sound && music) {
            game.musicStart();
        }
    }

    private void onGameOver(Game game, @Nullable Player winner) {
        if (game.isState(GameState.GAME)) {
            doEnd();
            game.endGame();
            repaint();
            String message = winner == null ? messages.get(MSG_DRAW) : messages.format(MSG__WINNER_S, winner.getName());
            JOptionPane.showMessageDialog(this, message);
        }
    }

    @SuppressWarnings("MagicNumber")
    private void onNewGame(Player... players) {
        game.beginGame(BOARD_SIZE, BOARD_WEIGHT, players);
        doBegin();
        repaint();
    }

    public void showMe() {
        setVisible(true);
        requestFocus();
    }
}
