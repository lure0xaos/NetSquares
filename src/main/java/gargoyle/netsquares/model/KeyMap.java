package gargoyle.netsquares.model;

import gargoyle.netsquares.util.Maps;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeyMap {
    private final Map<Action, Set<Integer>> defaultKeys = Maps.multiMapset(Action.class, Integer.class,
            Action.UP, KeyEvent.VK_UP,
            Action.DOWN, KeyEvent.VK_DOWN,
            Action.LEFT, KeyEvent.VK_LEFT,
            Action.RIGHT, KeyEvent.VK_RIGHT,
            Action.FIRE, KeyEvent.VK_SPACE, KeyEvent.VK_ENTER
    );
    private final Map<Action, Set<Integer>> keys = new HashMap<>(defaultKeys);

    public KeyMap() {
    }

    public KeyMap(Map<Action, Set<Integer>> keys) {
        this.keys.putAll(keys);
    }

    public Action actionForKey(int keyCode) {
        return Maps.keyForValueMultiMapset(keys, keyCode);
    }

    public Map<Action, Set<Integer>> getKeys() {
        return Collections.unmodifiableMap(keys);
    }

    public boolean isValid() {
        return Arrays.stream(Action.values()).map(keys::get).noneMatch(codes -> codes == null || codes.isEmpty());
    }

    public Set<Integer> keyForAction(Action action) {
        return keys.get(action);
    }

    public void setKey(Action action, int keyCode) {
        keys.get(action);
    }
}
