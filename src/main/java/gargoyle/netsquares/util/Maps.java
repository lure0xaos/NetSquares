package gargoyle.netsquares.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings({"", "unchecked", "Duplicates"})
public final class Maps {
    private Maps() {
    }

    private static @NotNull <K, V> Map<K, V> fill(@NotNull Map<K, V> map, Object... data) {
        for (int i = 0; i < data.length; i += 2) {
            map.put((K) data[i], (V) data[i + 1]);
        }
        return map;
    }

    public static <K, V> K keyForValue(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(e -> Objects.equals(e.getValue(), value)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public static <K, V> K keyForValueMultiMap(Map<K, List<V>> map, V value) {
        return map.entrySet().stream().filter(e -> e.getValue().contains(value)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public static <K, V> K keyForValueMultiMapset(Map<K, Set<V>> map, V value) {
        return map.entrySet().stream().filter(e -> e.getValue().contains(value)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public static <K, V> List<K> keysForValue(Map<K, V> map, V value) {
        return map.entrySet().stream().filter(e -> Objects.equals(e.getValue(), value)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <K, V> List<K> keysForValueMultiMap(Map<K, List<V>> map, V value) {
        return map.entrySet().stream().filter(e -> e.getValue().contains(value)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <K, V> List<K> keysForValueMultiMapset(Map<K, Set<V>> map, V value) {
        return map.entrySet().stream().filter(e -> e.getValue().contains(value)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <K, V> Map<K, V> map(Object... data) {
        return Collections.unmodifiableMap(fill(new HashMap<>(), data));
    }

    public static @NotNull <K, V> Map<K, V> mapFrom(Supplier<Map<K, V>> source, Object... data) {
        return fill(source.get(), data);
    }

    public static <K, V> Map<K, List<V>> multiMap(@NotNull Class<K> keyType, @NotNull Class<V> valueType, Object... data) {
        //noinspection Convert2MethodRef
        return multiMapFrom(keyType, valueType, () -> new HashMap<>(), () -> new ArrayList<>(), data);
    }

    public static @NotNull <K, V> Map<K, List<V>> multiMapAdd(@NotNull Map<K, List<V>> map, K key, V value) {
        return multiMapAdd(ArrayList::new, map, key, value);
    }

    private static @NotNull <K, V> Map<K, List<V>> multiMapAdd(@NotNull Supplier<List<V>> listSource, Map<K, List<V>> map, K key, V value) {
        map.computeIfAbsent(key, k -> listSource.get()).add(value);
        return map;
    }

    @SafeVarargs
    public static @NotNull <K, V> Map<K, List<V>> multiMapAddAll(@NotNull Map<K, List<V>> map, K key, V... values) {
        return multiMapAddAll(ArrayList::new, map, key, values);
    }

    @SafeVarargs
    private static @NotNull <K, V> Map<K, List<V>> multiMapAddAll(@NotNull Supplier<List<V>> listSource, Map<K, List<V>> map, K key, V... values) {
        map.computeIfAbsent(key, k -> listSource.get()).addAll(Arrays.asList(values));
        return map;
    }

    private static <K, V> Map<K, List<V>> multiMapFrom(@NotNull Class<K> keyType, @NotNull Class<V> valueType, Supplier<Map<K, List<V>>> mapSource, @NotNull Supplier<List<V>> listSource, Object... data) {
        Map<K, List<V>> map = mapSource.get();
        K key = null;
        for (Object el : data) {
            if (keyType.isInstance(el)) {
                key = (K) el;
            } else if (valueType.isInstance(el)) {
                map.computeIfAbsent(key, k -> listSource.get()).add((V) el);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return map;
    }

    public static <K, V> Map<K, Set<V>> multiMapset(@NotNull Class<K> keyType, @NotNull Class<V> valueType, Object... data) {
        //noinspection Convert2MethodRef
        return multiMapsetFrom(keyType, valueType, () -> new HashMap<>(), () -> new HashSet<>(), data);
    }

    public static @NotNull <K, V> Map<K, Set<V>> multiMapsetAdd(@NotNull Map<K, Set<V>> map, K key, V value) {
        return multiMapsetAdd(HashSet::new, map, key, value);
    }

    private static @NotNull <K, V> Map<K, Set<V>> multiMapsetAdd(@NotNull Supplier<Set<V>> listSource, Map<K, Set<V>> map, K key, V value) {
        map.computeIfAbsent(key, k -> listSource.get()).add(value);
        return map;
    }

    @SafeVarargs
    public static @NotNull <K, V> Map<K, Set<V>> multiMapsetAddAll(@NotNull Map<K, Set<V>> map, K key, V... values) {
        return multiMapsetAddAll(HashSet::new, map, key, values);
    }

    @SafeVarargs
    private static @NotNull <K, V> Map<K, Set<V>> multiMapsetAddAll(@NotNull Supplier<Set<V>> listSource, Map<K, Set<V>> map, K key, V... values) {
        map.computeIfAbsent(key, k -> listSource.get()).addAll(Arrays.asList(values));
        return map;
    }

    private static <K, V> Map<K, Set<V>> multiMapsetFrom(@NotNull Class<K> keyType, @NotNull Class<V> valueType, Supplier<Map<K, Set<V>>> mapSource, @NotNull Supplier<Set<V>> listSource, Object... data) {
        Map<K, Set<V>> map = mapSource.get();
        K key = null;
        for (Object el : data) {
            if (keyType.isInstance(el)) {
                key = (K) el;
            } else if (valueType.isInstance(el)) {
                map.computeIfAbsent(key, k -> listSource.get()).add((V) el);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return map;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public static <S, T> List<T> transform(List<S> source, Function<S, T> mapper) {
        return Collections.unmodifiableList(source.stream().map(mapper).collect(Collectors.toList()));
    }
}
