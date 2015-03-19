package net.declension.collections;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static net.declension.collections.CollectionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilsTest {

    public static final List<Integer> FIVE_VALUES_TOTAL_7 = asList(-1, 1, 2, 2, 3);
    public static final List<Integer> CONTAINING_ZERO_TOTAL_1 = asList(0, 1, 0);

    @Test
    public void totalOfHappyPath() {
        assertThat(totalOf(CONTAINING_ZERO_TOTAL_1)).isEqualTo(1);
        assertThat(totalOf(FIVE_VALUES_TOTAL_7)).isEqualTo(7);
    }

    @Test
    public void containsNoNullsShouldWork() {
        assertThat(containsNoNulls(FIVE_VALUES_TOTAL_7)).isTrue();
        assertThat(containsNoNulls(asList(1, 2, null, 4))).isFalse();
    }

    @Test
    public void chooseLowestSquareUsingFunctionShouldWork() {
        Function<Integer, Double> function = i -> i == 1? 10.0: 100.0;
        assertThat(chooseLowestSquareUsingFunction(FIVE_VALUES_TOTAL_7, function)).isEqualTo(1);
    }

    @Test
    public void chooseLowestSquareUsingFunctionShouldChooseFromLinear() {
        Function<Integer, Double> function = i -> 2.3 - i;
        assertThat(chooseLowestSquareUsingFunction(FIVE_VALUES_TOTAL_7, function)).isEqualTo(2);
    }

    @Test
    public void chooseLowestSquareUsingFunctionShouldChooseNegativesToo() {
        Function<Integer, Double> function = i -> -1.0 - i;
        assertThat(chooseLowestSquareUsingFunction(FIVE_VALUES_TOTAL_7, function)).isEqualTo(-1);
    }
}