package gargoyle.netsquares.util;

import java.util.Random;


public final class Rnd {
    private static final Random r = new Random();

    private Rnd() {
    }

    public static int rnd(int a, int b) {
        return r.nextInt(b - a + 1) + a;
    }

    private static <T> T rnd(T[] array) {
        return array[r.nextInt(array.length)];
    }

    public static <T extends Enum<T>> T rnd(Class<T> enumClass) {
        return rnd(enumClass.getEnumConstants());
    }
}
