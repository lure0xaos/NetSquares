package gargoyle.netsquares.res;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class AudioClipImpl implements AudioClip {
    private final Clip clip;

    public AudioClipImpl(URL location) {
        Clip clip = null;
        try {
            try (InputStream stream = location.openConnection().getInputStream();
                 AudioInputStream sound = AudioSystem.getAudioInputStream(stream)) {
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            clip = null;
            throw new RuntimeException(e.getLocalizedMessage(), e);
        } finally {
            this.clip = clip;
        }
    }

    @Override
    public void close() {
        if (clip != null && clip.isOpen()) {
            clip.close();
        }
    }

    @Override
    public void loop() {
        if (clip != null && clip.isOpen()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void play() {
        if (clip != null && clip.isOpen()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    @Override
    public void stop() {
        if (clip != null && clip.isOpen()) {
            clip.stop();
        }
    }
}
