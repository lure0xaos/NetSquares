package gargoyle.netsquares.model

import gargoyle.netsquares.util.Maps
import java.awt.event.KeyEvent

class KeyMap {
    private val defaultKeys = Maps.multiMapSet(
        Action::class, Int::class,
        Action.UP, KeyEvent.VK_UP,
        Action.DOWN, KeyEvent.VK_DOWN,
        Action.LEFT, KeyEvent.VK_LEFT,
        Action.RIGHT, KeyEvent.VK_RIGHT,
        Action.FIRE, KeyEvent.VK_SPACE, KeyEvent.VK_ENTER
    )
    private val keys: MutableMap<Action?, MutableSet<Int>> = defaultKeys.toMutableMap()

    constructor()
    constructor(keys: Map<Action?, MutableSet<Int>>) {
        this.keys.putAll(keys)
    }

    fun actionForKey(keyCode: Int): Action? {
        return Maps.keyForValueMultiMapSetAny(keys, keyCode)
    }

    fun getKeys(): Map<Action?, MutableSet<Int>> {
        return keys
    }

    val isValid: Boolean
        get() = Action.values().map { keys[it] }.none { it == null || it.isEmpty() }

    fun keyForAction(action: Action?): Set<Int?>? {
        return keys[action]
    }

    fun setKey(action: Action?, keyCode: Int) {
        keys[action] = mutableSetOf(keyCode)
    }
}
