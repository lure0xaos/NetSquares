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
public class AnyCache<K, V> {
    private final Map<K, Reference<V>> cache = new HashMap<>();

    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Loader<K, T> loader) {
        return get(name, loader, Locale.getDefault());
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T extends V> T get(@NotNull K name, @NotNull Loader<K, T> loader, @NotNull Locale locale) {
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
}
