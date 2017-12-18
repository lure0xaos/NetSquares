package gargoyle.netsquares.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public final class SerializationUtils {
    private SerializationUtils() {
    }

    public static <T> T pipe(T object) {
        return deserialize(serialize(object));
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        //noinspection SpellCheckingInspection
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> byte[] serialize(T object) {
        //noinspection SpellCheckingInspection
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        try (ObjectOutput oos = new ObjectOutputStream(bais)) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return bais.toByteArray();
    }
}
