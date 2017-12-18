package gargoyle.netsquares.prop.simple

import gargoyle.netsquares.prop.RWProperty

class BooleanProperty(value: Boolean = false) : RWProperty<Boolean>(value) {

    var value: Boolean
        get() = get()
        set(value) {
            set(value)
        }
}
