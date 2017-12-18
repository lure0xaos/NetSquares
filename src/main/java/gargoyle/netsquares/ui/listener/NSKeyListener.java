package gargoyle.netsquares.ui.listener;

import gargoyle.netsquares.model.Action;
import org.jetbrains.annotations.Nullable;

public interface NSKeyListener {
    void onKeyPress(@Nullable Action action);

    void onKeyRelease(@Nullable Action action);
}
