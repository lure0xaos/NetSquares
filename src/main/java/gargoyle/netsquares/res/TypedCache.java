package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class TypedCache<K, V> {
    private final AnyCache<K, V> cache = new AnyCache<>();
    private final Map<Class<? extends V>, Loader<K, ? extends V>> loaders = new HashMap<>();

    public TypedCache() {
    }

    public TypedCache(@NotNull Map<Class<? extends V>, Loader<K, ? extends V>> loaders) {
        this.loaders.putAll(loaders);
    }

    public <T extends V> void addLoader(@NotNull Class<T> type, @NotNull Loader<K, T> loader) {
        loaders.put(type, loader);
    }

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Class<T> type) {
        Loader<K, T> loader = getLoader(type);
        Assert.assertNotNull(loader, "loader is not known for {0}", type);
        return cache.get(name, loader);
    }

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Class<T> type, @NotNull Locale locale) {
        Loader<K, T> loader = getLoader(type);
        Assert.assertNotNull(loader, "loader is not known for {0}", type);
        return cache.get(name, loader, locale);
    }

    @SuppressWarnings("unchecked")
    private @NotNull <T extends V> Loader<K, T> getLoader(Class<T> type) {
        Loader<K, ? extends V> Loader = loaders.get(type);
        return (Loader<K, T>) Loader;
    }
}
