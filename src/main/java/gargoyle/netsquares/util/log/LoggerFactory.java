package gargoyle.netsquares.util.log;

public final class LoggerFactory {
    public static Logger getLogger(Class<?> aClass) {
        return getLogger(aClass.getName());
    }

    public static Logger getLogger(String name) {
        return new JULLogger(name);
    }
}
