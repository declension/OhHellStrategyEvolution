package net.declension.collections;

import com.google.common.collect.ImmutableSet;

import java.util.*;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class SlotsMap<K,V> implements Map<K,V> {
    /**
     * Stores the keys in order.,
     */
    private final LinkedHashSet<K> allKeys;
    private final Supplier<V> defaultSupplier;
    private final int capacity;
    Map<K,V> delegate;

    public SlotsMap(Collection<K> allKeys) {
        this(allKeys, (V) null);
    }

    public SlotsMap(Collection<K> allKeys, V defaultValue) {
        this(allKeys, () -> defaultValue);
    }

    public SlotsMap(Collection<K> allKeys, Supplier<V> defaultSupplier) {
        this.defaultSupplier = defaultSupplier;
        if (allKeys == null || allKeys.size() < 1) {
            throw new IllegalArgumentException(SlotsMap.class.getSimpleName() + " must have at least one key");
        }
        this.allKeys = new LinkedHashSet<>(allKeys);

        capacity = allKeys.size();
        delegate = new HashMap<>(allKeys.size());
    }

    public boolean isDone() {
        return capacity == size();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return allKeys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        checkKey(key);
        V value = delegate.get(key);
        if (value == null) {
            value = defaultSupplier.get();
            delegate.put((K) key, value);
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        checkKey(key);
        return delegate.put(key, value);
    }

    private void checkKey(Object key) {
        if (!allKeys.contains(key)) {
            throw new IllegalArgumentException(String.format("Unknown key: %s. Try: %s", key, allKeys));
        }
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Can't remove from " + getClass().getName());
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> incoming) {
        incoming.entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return ImmutableSet.copyOf(allKeys);
    }

    @Override
    public Collection<V> values() {
        return allKeys.stream()
                .map(delegate::get)
                .collect(toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return allKeys.stream()
                .map(k -> new AbstractMap.SimpleEntry<>(k, delegate.get(k)))
                .collect(toCollection((LinkedHashSet::new)));
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

    public static <K, V> SlotsMap<K, V> fromMap(Map<K, V> input) {
        SlotsMap<K, V> ret = new SlotsMap<>(input.keySet());
        ret.putAll(input);
        return ret;
    }

    /**
     * Returns a copy of this map with a single key adde3d.
     * @param key the new key
     * @param value the value for that new key
     * @return a copy of this map with the additional update
     */
    public SlotsMap<K, V> copyWithEntry(K key, V value) {
        Set<K> keys = new HashSet<>(keySet());
        keys.add(key);
        SlotsMap<K, V> ret = new SlotsMap<>(keys, defaultSupplier);
        ret.put(key, value);
        return ret;
    }
}
