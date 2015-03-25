package net.declension.ea.cards.ohhell;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RangeTest {

    public static final Range RANGE_2 = new Range(2, 0, 3);
    public static final Range RANGE_1 = new Range(1, 0, 10);

    @Test
    public void compareToShould() {
        assertThat(RANGE_2).isGreaterThan(RANGE_1);
    }

    @Test
    public void minAndMaxShouldWork() {
        assertThat(RANGE_2.min()).isEqualTo(0);
        assertThat(RANGE_2.max()).isEqualTo(3);
    }

    @Test
    public void numberMethodsShouldWork() {
        assertThat(RANGE_2.intValue()).isEqualTo(2);
        assertThat(RANGE_2.doubleValue()).isEqualTo(2.0);
        assertThat(RANGE_2.longValue()).isEqualTo(2L);
        assertThat(RANGE_2.floatValue()).isEqualTo(2.0f);
    }

    @Test
    public void constructShouldBorkIfOutOfRange() {
        assertThatThrownBy(() -> new Range(0, 1, 10)).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void toStringShouldMentionRange() {
        assertThat(new Range(2, 1, 100).toString())
                .contains("2")
                .contains("1|2|100");

    }
}