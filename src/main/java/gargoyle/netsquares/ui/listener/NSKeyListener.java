package gargoyle.netsquares.ui.listener;

import gargoyle.netsquares.model.Action;

public interface NSKeyListener {
    void onKeyPress(Action action);

    void onKeyRelease(Action action);
}
