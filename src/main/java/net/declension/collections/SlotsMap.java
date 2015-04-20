package net.declension.collections;

import com.google.common.collect.ImmutableSet;

import java.util.*;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * A map that has a fixed set of allowed keys, maintaining these as "slots" which are either
 * filled or waiting for data.
 *
 * The {@link #keySet()}, {@link #entrySet()} and {@link #toString()} behave unusually,
 * in that they will report keys for items not yet inserted (e.g. for empty sets).
 * This is crucial to the usefulness of the class.
 *
 * {@link #size()} and {@link #isEmpty()} behave fairly normally for a map, but there
 * is a method {@link #capacity()} that returns the eventual size of the collection,
 * and {@link #remaining()} to return the difference between these.
 *
 * Putting data against unknown keys will throw exceptions.
 *
 * @param <K> The key type
 * @param <V> the value type.
 * @author Nick Boultbee
 */
public class SlotsMap<K,V> implements Map<K,V> {
    /**
     * Stores the keys in order.,
     */
    private final LinkedHashSet<? extends K> allKeys;
    private final int capacity;
    final Map<K,V> delegate;
    private final V defaultValue;

    /**
     * Null will be the default value, as per "normal" maps.
     * @param allKeys
     */
    public SlotsMap(Collection<? extends K> allKeys) {
        this(allKeys, null);
    }

    /**
     * Construct a map with the given keys an explicit default
     * @param allKeys
     * @param defaultValue
     */
    public SlotsMap(Collection<? extends K> allKeys, V defaultValue) {
        this.defaultValue = defaultValue;
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
        checkKey((K) key);
        return delegate.getOrDefault(key, defaultValue);
    }

    @Override
    public V put(K key, V value) {
        checkKey(key);
        return delegate.put(key, value);
    }

    private void checkKey(K key) {
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
                .map(this::get)
                .collect(toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return allKeys.stream()
                .map(k -> new AbstractMap.SimpleEntry<>(k, get(k)))
                .collect(toCollection((LinkedHashSet::new)));
    }

    @Override
    public String toString() {
        return entrySet().toString();
    }

    /**
     * Gets the capacity of this object, i.e. how many Slots there are to fill.
     * This is based on the keys passed in at construction.
     *
     * @return a positive integer
     */
    public int capacity() {
        return capacity;
    }

    /**
     * The number of slots remaining to be filled.
     * @return an integer between 0 and {@link #capacity}
     */
    public int remaining() {
        return capacity - size();
    }
}
