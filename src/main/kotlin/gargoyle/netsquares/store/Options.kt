package gargoyle.netsquares.store

import gargoyle.netsquares.prop.base.BaseRWProperty
import gargoyle.netsquares.store.prop.PrefBooleanProperty
import gargoyle.netsquares.store.prop.PrefStringProperty
import kotlin.reflect.KClass

class Options(pkg: KClass<*>, defaultPlayerName: String) {
    private val music: BaseRWProperty<Boolean>
    private val player1: BaseRWProperty<String>
    private val player2: BaseRWProperty<String>
    private val sound: BaseRWProperty<Boolean>

    init {
        music = PrefBooleanProperty(pkg, KEY_MUSIC, DEF_MUSIC)
        sound = PrefBooleanProperty(pkg, KEY_SOUND, DEF_SOUND)
        player1 = PrefStringProperty(pkg, KEY_PLAYER_1, defaultPlayerName)
        player2 = PrefStringProperty(pkg, KEY_PLAYER_2, defaultPlayerName)
    }

    fun getPlayer1(): String {
        return player1.get()
    }

    fun setPlayer1(player1: String?) {
        if (player1 != null) {
            this.player1.set(player1)
        }
    }

    fun getPlayer2(): String {
        return player2.get()
    }

    fun setPlayer2(player2: String?) {
        if (player2 != null) {
            this.player2.set(player2)
        }
    }

    fun isMusic(): Boolean {
        return music.get()
    }

    fun setMusic(music: Boolean) {
        this.music.set(music)
    }

    fun isSound(): Boolean {
        return sound.get()
    }

    fun setSound(sound: Boolean) {
        this.sound.set(sound)
    }

    fun music(): BaseRWProperty<Boolean> {
        return music
    }

    fun player1(): BaseRWProperty<String> {
        return player1
    }

    fun player2(): BaseRWProperty<String> {
        return player2
    }

    fun sound(): BaseRWProperty<Boolean> {
        return sound
    }

    companion object {
        private const val DEF_MUSIC = true
        private const val DEF_SOUND = true
        private const val KEY_MUSIC = "music"
        private const val KEY_PLAYER_1 = "player1"
        private const val KEY_PLAYER_2 = "player2"
        private const val KEY_SOUND = "gargoyle/netsquares/res/sound"
    }
}
