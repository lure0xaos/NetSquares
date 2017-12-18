package gargoyle.netsquares;

import gargoyle.netsquares.res.Messages;
import gargoyle.netsquares.res.NSContext;
import gargoyle.netsquares.res.NSContextFactory;
import gargoyle.netsquares.store.Options;
import gargoyle.netsquares.ui.NSFrame;
import gargoyle.netsquares.ui.painter.NSPainter;

public final class NetSquares {
    private static final String ENV_USER_NAME = "user.name";
    private static final String MSG_PLAYER_HUMAN = "player.human";
    private static final String RES_MESSAGES = "messages";
    private static final String RES_BACKGROUND = "img";
    private static final String RES_BACKGROUND_SUFFIX = "jpg";
    private final Messages messages;
    private final Options options;
    private final NSPainter painter;
    private final NSContext context;
    @SuppressWarnings("FieldCanBeLocal")
    private NSFrame frame;

    private NetSquares(NSContext context) {
        this.context = context;
        messages = context.loadMessages(RES_MESSAGES);
        painter = new NSPainter(context, RES_BACKGROUND, RES_BACKGROUND_SUFFIX);
        options = new Options(NetSquares.class, System.getProperty(ENV_USER_NAME));
    }

    public static void main(String[] args) {
        new NetSquares(NSContextFactory.currentContext()).start();
    }

    private void start() {
        frame = new NSFrame(context, messages, painter, options);
        frame.showMe();
    }
}
