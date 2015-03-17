package net.declension.collections;

import com.google.common.collect.ImmutableList;

import java.util.AbstractSequentialList;
import java.util.ListIterator;

public class CircularList<T> extends AbstractSequentialList<T> {
    private final ImmutableList<T> items;

    public CircularList(Iterable<T> incoming) {
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
            this.index = index;
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
