package gargoyle.netsquares.ui.listener

import gargoyle.netsquares.model.Action

interface NSKeyListener {
    fun onKeyPress(action: Action)
    fun onKeyRelease(action: Action)
}
