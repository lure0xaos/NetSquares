package gargoyle.netsquares.beans;

import gargoyle.netsquares.ioc.Registry;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public final class Messages {

    private final ResourceBundle bundle;

    private Messages(final String baseName) {
        ResourceBundle bundle=null;
        try {
            bundle = ResourceBundle.getBundle(baseName, Control.getControl(Control.FORMAT_PROPERTIES));
        } catch (MissingResourceException e) {
            e.printStackTrace();
        }
        this.bundle=bundle;
    }

    public static Messages messages(final Class<?> baseClass) throws ClassNotFoundException {
        return Registry.forClass(Messages.class, "messages0").provide(baseClass.getName());
    }

    public static Messages messages(final String baseName) throws ClassNotFoundException {
        return Registry.forClass(Messages.class, "messages0").provide(baseName);
    }

    public static Messages messages0(final String baseName) {
        return new Messages(baseName);
    }

    public String message(final String message, final Object... args) {
        if (bundle == null || !bundle.containsKey(message)) {
            return message;
        }
        try {
            if (args.length == 0) {
                return bundle.getString(message);
            }
            return MessageFormat.format(bundle.getString(message), args);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
            return message;
        }
    }

}
