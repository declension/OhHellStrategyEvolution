package net.declension.collections;

import com.google.common.collect.ImmutableList;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

/**
 * A sequential list that wraps around, but only once; its cardinality remains identical to the wrapped delegate.
 * It can be used iterating either forwards (using {@link java.util.ListIterator#next()}),
 * or backwards (using {@link java.util.ListIterator#previous}).
 *
 * @param <T> the element type.
 */
public class ImmutableCircularList<T> extends AbstractSequentialList<T> {
    private final ImmutableList<T> items;

    public ImmutableCircularList(Iterable<T> incoming) {
        items = ImmutableList.copyOf(incoming);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ImmutableCircularIterator(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    private class ImmutableCircularIterator implements ListIterator<T> {
        private int index;
        private int count;

        public ImmutableCircularIterator(int index) {
            int n = size();
            this.index = (index % n + n) % n;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < size();
        }

        @Override
        public T next() {
            T item = items.get(index);
            index = nextIndex();
            count++;
            return item;
        }

        @Override
        public boolean hasPrevious() {
            return count > -size();
        }

        @Override
        public T previous() {
            T item = items.get(index);
            index = previousIndex();
            count--;
            return item;
        }

        @Override
        public int nextIndex() {
            return (index + 1) % size();
        }

        @Override
        public int previousIndex() {
            return (index + size() - 1) % size();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Immutable Iterator cannot remove");
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException("Immutable Iterator cannot set");
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException("Immutable Iterator cannot add");
        }
    }
}
