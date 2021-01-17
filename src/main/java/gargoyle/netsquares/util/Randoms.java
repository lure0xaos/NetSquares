package gargoyle.netsquares.util;

import java.util.Random;

public final class Randoms {
    private static final transient Random rnd = new Random();

    private Randoms() {
        super();
    }

    public static int random(final int from, final int to) {
        return rnd.nextInt(to - from) + from;
    }
}
