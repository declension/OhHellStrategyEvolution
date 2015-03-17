package net.declension.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImmutableCircularListTest {

    public static final int FIRST = 10;
    public static final int SECOND = 8;
    public static final int THIRD = 6;
    public static final List<Integer> EVENS = asList(FIRST, SECOND, THIRD);
    public static final List<Integer> CIRCULAR_EVENS = new ImmutableCircularList<>(EVENS);
    public static final ImmutableCircularList<?> EMPTY = new ImmutableCircularList<>(new ArrayList<>());

    @Test
    public void listIteratorShouldBehaveLikeIteratorAtZero() {
        ListIterator<Integer> itr = CIRCULAR_EVENS.listIterator(0);
        assertThat(itr.hasNext()).isTrue();
        assertThat(itr.hasPrevious()).isTrue();

        assertThat(itr.next()).isEqualTo(FIRST);
        assertThat(itr.next()).isEqualTo(SECOND);
        assertThat(itr.next()).isEqualTo(THIRD);

        assertThat(itr.hasNext()).isFalse();
        assertThat(itr.hasPrevious()).isTrue();
    }

    @Test
    public void listIteratorShouldWrapLargeIndices() {
        ListIterator<Integer> itr = CIRCULAR_EVENS.listIterator(EVENS.size());
        assertThat(itr.hasNext()).isTrue();
        assertThat(itr.next()).isEqualTo(FIRST);
    }


    @Test
    public void negativeIndiciesShouldWork() {
        assertThat(CIRCULAR_EVENS.listIterator(-1).next()).isEqualTo(THIRD);
        assertThat(CIRCULAR_EVENS.listIterator(-2).next()).isEqualTo(SECOND);
    }

    @Test
    public void listIteratorShouldWrapLargeNegativeIndices() {
        ListIterator<Integer> itr = CIRCULAR_EVENS.listIterator(-EVENS.size());
        assertThat(itr.hasNext()).isTrue();
        assertThat(itr.next()).isEqualTo(FIRST);
        assertThat(CIRCULAR_EVENS.listIterator(-EVENS.size() -1).next()).isEqualTo(THIRD);
    }

    @Test
    public void listIteratorShouldBehaveLikeIteratorAtPositive() {
        ListIterator<Integer> itr = CIRCULAR_EVENS.listIterator(2);
        assertThat(itr.hasNext()).isTrue();
        assertThat(itr.hasPrevious()).isTrue();

        assertThat(itr.next()).isEqualTo(THIRD);
        assertThat(itr.next()).isEqualTo(FIRST);
        assertThat(itr.next()).isEqualTo(SECOND);

        assertThat(itr.hasNext()).isFalse();
        assertThat(itr.hasPrevious()).isTrue();
    }

    @Test
    public void backwardsIterationShouldAlsoWork() {
        ListIterator<Integer> itr = CIRCULAR_EVENS.listIterator(2);

        assertThat(itr.hasPrevious()).isTrue();

        assertThat(itr.previous()).isEqualTo(THIRD);
        assertThat(itr.previous()).isEqualTo(SECOND);
        assertThat(itr.previous()).isEqualTo(FIRST);

        assertThat(itr.hasPrevious()).isFalse();
        assertThat(itr.hasNext()).isTrue();
    }

    @Test
    public void sizeShouldMatchInput() {
        assertThat(EMPTY).hasSize(0);
        assertThat(CIRCULAR_EVENS).hasSize(EVENS.size());
    }

    @Test
    public void isEmptyShouldBeTrueForEmptyInput() {
        assertThat(EMPTY).isEmpty();
    }

    @Test
    public void removeShouldThrow() {
        assertThatThrownBy(() -> CIRCULAR_EVENS.remove(2)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void addShouldThrow() {
        assertThatThrownBy(() -> CIRCULAR_EVENS.add(10)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void setShouldThrow() {
        assertThatThrownBy(() -> CIRCULAR_EVENS.set(0, 10)).isInstanceOf(UnsupportedOperationException.class);
    }

}