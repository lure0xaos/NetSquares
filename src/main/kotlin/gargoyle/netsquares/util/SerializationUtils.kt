package gargoyle.netsquares.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SerializationUtils {

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> deserialize(bytes: ByteArray?): T {
        ObjectInputStream(ByteArrayInputStream(bytes)).use { return it.readObject() as T }
    }

    fun <T : Any> serialize(obj: T): ByteArray =
        ByteArrayOutputStream().apply {
            ObjectOutputStream(this).use { it.writeObject(obj) }
        }.toByteArray()
}
