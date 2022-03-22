package gargoyle.netsquares.res

import java.io.Closeable

interface AudioClip : Closeable {
    override fun close() {}
    fun loop()
    fun play()
    fun stop()
}
