package net.declension.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CircularListTest {

    public static final int FIRST = 10;
    public static final int SECOND = 8;
    public static final int THIRD = 6;
    public static final List<Integer> EVENS = asList(FIRST, SECOND, THIRD);
    public static final CircularList<?> EMPTY = new CircularList<>(new ArrayList<>());

    @Test
    public void listIteratorShouldBehaveLikeIteratorAtZero() {
        ListIterator<Integer> itr = new CircularList<>(EVENS).listIterator(0);
        assertThat(itr.hasNext()).isTrue();
        assertThat(itr.hasPrevious()).isTrue();

        assertThat(itr.next()).isEqualTo(FIRST);
        assertThat(itr.next()).isEqualTo(SECOND);
        assertThat(itr.next()).isEqualTo(THIRD);

        assertThat(itr.hasNext()).isFalse();
        assertThat(itr.hasPrevious()).isTrue();
    }

    @Test
    public void listIteratorShouldBehaveLikeIteratorAtPositive() {
        ListIterator<Integer> itr = new CircularList<>(EVENS).listIterator(2);
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
        ListIterator<Integer> itr = new CircularList<>(EVENS).listIterator(2);

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
        assertThat(new CircularList<>(EVENS)).hasSize(EVENS.size());
    }

    @Test
    public void isEmptyShouldBeTrueForEmptyInput() {
        assertThat(EMPTY).isEmpty();
    }
}