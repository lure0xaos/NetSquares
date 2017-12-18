package gargoyle.netsquares.res;

import gargoyle.netsquares.util.Assert;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class HybridCache<K, V> {
    private final Map<K, Reference<V>> cache = new HashMap<>();
    private final Map<Class<? extends V>, Loader<K, ? extends V>> loaders = new HashMap<>();

    public HybridCache(Map<Class<? extends V>, Loader<K, ? extends V>> loaders) {
        this.loaders.putAll(loaders);
    }

    public <T extends V> T get(NSContext context, K name, Class<T> type, String suffix) {
        Loader<K, T> loader = getLoader(type);
        Assert.assertNotNull(loader, "loader is not known for {0}", type);
        return get(context, name, loader, suffix);
    }

    @SuppressWarnings("unchecked")
    private <T extends V> Loader<K, T> getLoader(Class<T> type) {
        Loader<K, ? extends V> Loader = loaders.get(type);
        return (Loader<K, T>) Loader;
    }

    @SuppressWarnings("unchecked")
    private <T extends V> T get(NSContext context, K name, Loader<K, T> loader, String suffix) {
        Assert.assertNotNull(name, "name is null");
        Assert.assertNotNull(loader, "loader is null");
        Reference<V> reference = cache.get(name);
        if (reference != null) {
            V value = reference.get();
            if (value != null) {
                return (T) value;
            }
        }
        try {
            T value = loader.load(context, name, suffix);
            Assert.assertNotNull(value, "loader did not load");
            cache.put(name, new SoftReference<>(value));
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
