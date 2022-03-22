package gargoyle.netsquares.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SerializationUtils {
    fun <T : Any> pipe(obj: T): T {
        return deserialize(serialize(obj))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> deserialize(bytes: ByteArray?): T {
        val bais = ByteArrayInputStream(bytes)
        ObjectInputStream(bais).use { ois -> return ois.readObject() as T }
    }

    fun <T : Any> serialize(obj: T): ByteArray {
        val bais = ByteArrayOutputStream()
        ObjectOutputStream(bais).use { oos -> oos.writeObject(obj) }
        return bais.toByteArray()
    }
}
