package gargoyle.netsquares.util;

public final class Strings {
    private Strings() {
        super();
    }

    public static String join(final String glue, final Object... args) {
        final StringBuilder sb = new StringBuilder();
        for (final Object arg : args) {
            if (sb.length() != 0) {
                sb.append(glue);
            }
            sb.append(arg);
        }
        return sb.toString();
    }

    public static String join(final String glue, final Iterable args) {
        final StringBuilder sb = new StringBuilder();
        for (final Object arg : args) {
            if (sb.length() != 0) {
                sb.append(glue);
            }
            sb.append(arg);
        }
        return sb.toString();
    }
}
