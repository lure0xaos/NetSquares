package gargoyle.netsquares.res

import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

internal class AudioClipImpl(location: URL?) : AudioClip {
    private val clip: Clip

    init {
        clip = (AudioSystem.getClip()).also {
            location!!.openConnection().getInputStream().use { stream ->
                AudioSystem.getAudioInputStream(stream).use { sound ->
                    it.open(sound)
                }
            }
        }
    }

    override fun close() {
        if (clip.isOpen) {
            clip.close()
        }
    }

    override fun loop() {
        if (clip.isOpen) {
            clip.loop(Clip.LOOP_CONTINUOUSLY)
        }
    }

    override fun play() {
        if (clip.isOpen) {
            clip.framePosition = 0
            clip.start()
        }
    }

    override fun stop() {
        if (clip.isOpen) {
            clip.stop()
        }
    }
}
