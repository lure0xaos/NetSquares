package gargoyle.netsquares.res;

import gargoyle.netsquares.util.log.Logger;
import gargoyle.netsquares.util.log.LoggerFactory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {
    private final Logger logger = LoggerFactory.getLogger(Messages.class);
    private transient ResourceBundle bundle;

    Messages(NSContext context, String name) {
        try {
            bundle = context.loadBundle(name);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s not found", name));
        }
    }

    public String format(String key, Object... args) {
        try {
            String s = bundle == null ? key : bundle.getString(key);
            return String.format(s, args);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s.%s not found", bundle == null ? null : bundle.getBaseBundleName(), key));
            return key;
        }
    }

    public String get(String key) {
        try {
            return bundle == null ? key : bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s.%s not found", bundle.getBaseBundleName(), key));
            return key;
        }
    }
}
