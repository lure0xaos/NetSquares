package gargoyle.netsquares.i18n;

import gargoyle.netsquares.res.Resources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import gargoyle.netsquares.util.log.Logger;
import gargoyle.netsquares.util.log.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages implements Serializable {
    private final @NotNull Locale locale;
    private final Logger logger = LoggerFactory.getLogger(Messages.class);
    private final @NotNull String name;
    private transient @Nullable ResourceBundle bundle;

    public Messages(@NotNull String name, @Nullable Locale locale) {
        this.name = name;
        this.locale = locale == null ? Locale.getDefault() : locale;
        try {
            bundle = Resources.loadBundle(name, this.locale);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s not found", name));
        }
    }

    public @NotNull String format(@NotNull String key, Object... args) {
        try {
            String s = bundle == null ? key : bundle.getString(key);
            return String.format(s, args);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s.%s not found", bundle == null ? null : bundle.getBaseBundleName(), key));
            return key;
        }
    }

    public @NotNull String get(@NotNull String key) {
        try {
            return bundle == null ? key : bundle.getString(key);
        } catch (MissingResourceException e) {
            logger.error(String.format("%s.%s not found", bundle.getBaseBundleName(), key));
            return key;
        }
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        bundle = Resources.loadBundle(name, locale);
    }
}
