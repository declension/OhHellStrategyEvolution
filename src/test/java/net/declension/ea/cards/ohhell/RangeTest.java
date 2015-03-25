package net.declension.ea.cards.ohhell;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RangeTest {

    @Test
    public void compareToShould() {

    }

    @Test
    public void numberMethodsShouldWork() {
        Range range = new Range(2, 0, 3);
        assertThat(range.intValue()).isEqualTo(2);
        assertThat(range.doubleValue()).isEqualTo(2.0);
        assertThat(range.longValue()).isEqualTo(2L);
        assertThat(range.floatValue()).isEqualTo(2.0f);
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