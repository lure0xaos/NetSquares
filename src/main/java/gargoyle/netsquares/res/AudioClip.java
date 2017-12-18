package gargoyle.netsquares.res;

import java.io.Closeable;

public interface AudioClip extends Closeable {
    @Override
    default void close() {
    }

    void loop();

    void play();

    void stop();
}
