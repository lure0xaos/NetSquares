package gargoyle.netsquares.store;

import gargoyle.netsquares.prop.base.BaseRWProperty;
import gargoyle.netsquares.store.prop.PrefBooleanProperty;
import gargoyle.netsquares.store.prop.PrefStringProperty;
import org.jetbrains.annotations.NotNull;

public class Options {
    private static final boolean DEF_MUSIC = true;
    private static final boolean DEF_SOUND = true;
    private static final String KEY_MUSIC = "music";
    private static final String KEY_PLAYER_1 = "player1";
    private static final String KEY_PLAYER_2 = "player2";
    private static final String KEY_SOUND = "sound";
    private final BaseRWProperty<Boolean> music;
    private final BaseRWProperty<String> player1;
    private final BaseRWProperty<String> player2;
    private final BaseRWProperty<Boolean> sound;

    public Options(Class<?> pkg, @NotNull String defaultPlayerName) {
        music = new PrefBooleanProperty(pkg, KEY_MUSIC, DEF_MUSIC);
        sound = new PrefBooleanProperty(pkg, KEY_SOUND, DEF_SOUND);
        player1 = new PrefStringProperty(pkg, KEY_PLAYER_1, defaultPlayerName);
        player2 = new PrefStringProperty(pkg, KEY_PLAYER_2, defaultPlayerName);
    }

    public String getPlayer1() {
        return player1.get();
    }

    public void setPlayer1(String player1) {
        if (player1 != null) {
            this.player1.set(player1);
        }
    }

    public String getPlayer2() {
        return player2.get();
    }

    public void setPlayer2(String player2) {
        if (player2 != null) {
            this.player2.set(player2);
        }
    }

    public boolean isMusic() {
        return music.get();
    }

    public void setMusic(boolean music) {
        this.music.set(music);
    }

    public boolean isSound() {
        return sound.get();
    }

    public void setSound(boolean sound) {
        this.sound.set(sound);
    }

    public final BaseRWProperty<Boolean> music() {
        return music;
    }

    public final BaseRWProperty<String> player1() {
        return player1;
    }

    public final BaseRWProperty<String> player2() {
        return player2;
    }

    public final BaseRWProperty<Boolean> sound() {
        return sound;
    }
}
