package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@FunctionalInterface
public interface Loader<L, R> {
    ClassLoader classLoader = Loader.class.getClassLoader();

    static @NotNull URL find(@NotNull String name) {
        URL resource = classLoader.getResource(name);
        Assert.assertNotNull(resource, String.format("%s not found", name));
        return resource;
    }

    static @NotNull URL findLocalized(@NotNull String name) {
        return findLocalized(name, Locale.getDefault());
    }

    static @NotNull URL findLocalized(@NotNull String name, @NotNull Locale locale) {
        int dot = name.lastIndexOf('.');
        String baseName = name.substring(0, dot < 0 ? name.length() : dot);
        String suffix = name.substring(dot + 1);
        return findLocalized(baseName, suffix, locale);
    }

    static @NotNull URL findLocalized(@NotNull String baseName, @NotNull String suffix) {
        return findLocalized(baseName, suffix, Locale.getDefault());
    }

    static @NotNull URL findLocalized(@NotNull String baseName, @NotNull String suffix, @NotNull Locale locale) {
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);
        List<Locale> candidateLocales = control.getCandidateLocales(baseName, locale);
        for (Locale specificLocale : candidateLocales) {
            String bundleName = control.toBundleName(baseName, specificLocale);
            String resourceName = control.toResourceName(bundleName, suffix);
            URL resource = classLoader.getResource(resourceName);
            if (resource != null) {
                return resource;
            }
        }
        throw new IllegalArgumentException(String.format("%s not found", baseName));
    }

    static @NotNull InputStream open(@NotNull String name) {
        InputStream resource = classLoader.getResourceAsStream(name);
        Assert.assertNotNull(resource, String.format("%s not found", name));
        return resource;
    }

    @NotNull
    R load(@NotNull L location, @NotNull Locale locale) throws Exception;

    default @Nullable R load(@NotNull L location) throws Exception {
        return load(location, Locale.getDefault());
    }

    default @Nullable R loadAndRethrow(@NotNull L location, @NotNull Locale locale) {
        try {
            return load(location, locale);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    default @Nullable R loadAndRethrow(@NotNull L location) {
        try {
            return load(location, Locale.getDefault());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
