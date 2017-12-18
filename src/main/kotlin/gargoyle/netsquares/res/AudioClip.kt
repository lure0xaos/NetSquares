package gargoyle.netsquares.res

import java.io.Closeable

interface AudioClip : Closeable {
    fun loop()
    fun play()
    fun stop()
}
