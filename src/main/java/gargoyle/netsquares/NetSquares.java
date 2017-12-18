package gargoyle.netsquares;

import gargoyle.netsquares.i18n.Messages;
import gargoyle.netsquares.store.Options;
import gargoyle.netsquares.ui.NSFrame;
import gargoyle.netsquares.ui.painter.NSPainter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class NetSquares {
    private static final String ENV_USER_NAME = "user.name";
    private static final String MSG_PLAYER_HUMAN = "player.human";
    private static final String RES_MESSAGES = "messages";
    private final @NotNull Messages messages;
    private final @NotNull Options options;
    private final @NotNull NSPainter painter;
    @SuppressWarnings("FieldCanBeLocal")
    private NSFrame frame;

    private NetSquares(Locale locale) {
        messages = new Messages(RES_MESSAGES, locale);
        painter = new NSPainter(locale);
        options = new Options(NetSquares.class, System.getProperty(ENV_USER_NAME));
    }

    public static void main(String[] args) {
        new NetSquares(Locale.getDefault()).start();
    }

    private void start() {
        frame = new NSFrame(messages, painter, options);
        frame.showMe();
    }
}
