package net.declension.utils;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class SlotsMap<K,V> implements Map<K,V> {
    /**
     * Stores the keys in order.,
     */
    private final LinkedHashSet<K> allKeys;
    private final int capacity;
    Map<K,V> delegate;

    public SlotsMap(Collection<K> allKeys) {
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
        return delegate.get(key);
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
        throw new IllegalArgumentException("Can't clear");
    }

    @Override
    public Set<K> keySet() {
        return allKeys;
    }

    @Override
    public Collection<V> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return keySet().stream()
                .map(k -> new AbstractMap.SimpleEntry<K, V>(k, delegate.get(k)))
                .collect(toSet());
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
