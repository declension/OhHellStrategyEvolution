package net.declension.collections;

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
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Can't put all in " + getClass().getName());
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Set<K> keySet() {
        return allKeys;
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
}
