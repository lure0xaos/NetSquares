package gargoyle.netsquares.model.player;

import gargoyle.netsquares.model.*;
import gargoyle.netsquares.ui.listener.NSKeyListener;
import gargoyle.netsquares.ui.listener.NSMouseListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HumanPlayer extends Player implements NSKeyListener, NSMouseListener {
    private final Collection<Action> persistentActions = new LinkedHashSet<>();
    private final Queue<Action> stackedActions = new ArrayDeque<>();
    private KeyMap keyMap = new KeyMap();

    public HumanPlayer(String name) {
        super(name);
    }

    public KeyMap getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(KeyMap keyMap) {
        this.keyMap = keyMap;
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    protected Move move(@NotNull Board board, @NotNull Directions directions) {
        if (stackedActions.isEmpty()) {
            Iterator<Action> iterator = persistentActions.iterator();
            if (iterator.hasNext()) {
                Action action = iterator.next();
                Move move = action.getMove();
                if (move.isAllowed(directions)) {
                    if (move == Move.OPEN) {
                        persistentActions.clear();
                    }
                    return move;
                }
            }
            return null;
        } else {
            return stackedActions.poll().getMove();
        }
    }

    @Override
    public void onClick(Action[] actions) {
        stackedActions.addAll(Arrays.asList(actions));
    }

    @Override
    public void onKeyPress(@Nullable Action action) {
        if (action != null) {
            persistentActions.add(action);
        }
    }

    @Override
    public void onKeyRelease(@Nullable Action action) {
        if (action != null) {
            persistentActions.remove(action);
        }
    }
}
