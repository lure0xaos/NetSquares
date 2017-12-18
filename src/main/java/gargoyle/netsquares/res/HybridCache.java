package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Assert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class HybridCache<K, V> {
    private final Map<K, Reference<V>> cache = new HashMap<>();
    private final Map<Class<? extends V>, Loader<K, ? extends V>> loaders = new HashMap<>();

    public HybridCache() {
    }

    public HybridCache(@NotNull Map<Class<? extends V>, Loader<K, ? extends V>> loaders) {
        this.loaders.putAll(loaders);
    }

    public <T extends V> void addLoader(@NotNull Class<T> type, @NotNull Loader<K, T> loader) {
        loaders.put(type, loader);
    }

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Class<T> type) {
        return get(name, type, Locale.getDefault());
    }

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Loader<K, T> loader) {
        return get(name, loader, Locale.getDefault());
    }

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Class<T> type, @NotNull Locale locale) {
        Loader<K, T> loader = getLoader(type);
        Assert.assertNotNull(loader, "loader is not known for {0}", type);
        return get(name, loader, locale);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private <T extends V> T get(@NotNull K name, @NotNull Loader<K, T> loader, @NotNull Locale locale) {
        Assert.assertNotNull(name, "name is null");
        Assert.assertNotNull(loader, "loader is null");
        Reference<V> reference = cache.get(name);
        if (reference != null) {
            V value = reference.get();
            if (value != null) {
                return (T) value;
            }
        }
        T value = loader.loadAndRethrow(name, locale);
        Assert.assertNotNull(value, "loader did not load");
        cache.put(name, new SoftReference<>(value));
        return value;
    }

    @SuppressWarnings("unchecked")
    private @NotNull <T extends V> Loader<K, T> getLoader(Class<T> type) {
        Loader<K, ? extends V> Loader = loaders.get(type);
        return (Loader<K, T>) Loader;
    }
}
