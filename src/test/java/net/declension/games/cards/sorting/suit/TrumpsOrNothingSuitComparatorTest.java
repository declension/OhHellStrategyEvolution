package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;
import org.junit.Before;
import org.junit.Test;

import static net.declension.games.cards.Suit.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsOrNothingSuitComparatorTest {


    public static final Suit TRUMPS = HEARTS;
    private TrumpsOrNothingSuitComparator cmp;

    @Before
    public void setUp() throws Exception {
        cmp = new TrumpsOrNothingSuitComparator(TRUMPS);
    }

    @Test
    public void trumpsShouldBeatNonTrumps() {
        assertThat(HEARTS).usingComparator(cmp)
                .isGreaterThan(DIAMONDS)
                .isGreaterThan(SPADES)
                .isGreaterThan(CLUBS);
    }

    @Test
    public void trumpsShouldBeEqual() {
        assertThat(HEARTS).usingComparator(cmp).isEqualTo(HEARTS);
    }

    @Test
    public void nonTrumpsShouldBeEqual() {
        assertThat(DIAMONDS).usingComparator(cmp)
                .isEqualTo(CLUBS)
                .isEqualTo(SPADES);
    }

}