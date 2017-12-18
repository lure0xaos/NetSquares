package gargoyle.netsquares.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public final class SerializationUtils {
    private SerializationUtils() {
    }

    @SuppressWarnings("unchecked")
    public static @NotNull <T> T deserialize(@NotNull byte[] bytes) {
        //noinspection SpellCheckingInspection
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (@NotNull IOException | ClassNotFoundException | ClassCastException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static @NotNull <T> T pipe(T object) {
        return deserialize(serialize(object));
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
