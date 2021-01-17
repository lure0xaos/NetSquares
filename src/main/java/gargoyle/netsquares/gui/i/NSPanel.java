package gargoyle.netsquares.gui.i;

import gargoyle.netsquares.events.IGameNotifier;
import gargoyle.netsquares.logic.i.IGame;

public interface NSPanel extends IGameNotifier{
    void bind(IGame game);

    void unbind(IGame game);
}
