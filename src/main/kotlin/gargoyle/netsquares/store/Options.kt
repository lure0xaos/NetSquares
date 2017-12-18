package gargoyle.netsquares.store

import gargoyle.netsquares.prop.base.BaseRWProperty
import gargoyle.netsquares.store.prop.PrefBooleanProperty
import gargoyle.netsquares.store.prop.PrefStringProperty
import kotlin.reflect.KClass

class Options(pkg: KClass<*>, defaultPlayerName: String) {
    val player1: BaseRWProperty<String> = PrefStringProperty(pkg, KEY_PLAYER_1, defaultPlayerName)
    val player2: BaseRWProperty<String> = PrefStringProperty(pkg, KEY_PLAYER_2, defaultPlayerName)
    val music: BaseRWProperty<Boolean> = PrefBooleanProperty(pkg, KEY_MUSIC, DEF_MUSIC)
    val sound: BaseRWProperty<Boolean> = PrefBooleanProperty(pkg, KEY_SOUND, DEF_SOUND)

    companion object {
        private const val DEF_MUSIC = true
        private const val DEF_SOUND = true
        private const val KEY_MUSIC = "music"
        private const val KEY_PLAYER_1 = "player1"
        private const val KEY_PLAYER_2 = "player2"
        private const val KEY_SOUND = "gargoyle/netsquares/res/sound"
    }
}
