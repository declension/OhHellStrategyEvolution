package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsFirstSuitComparatorTest {

    private TrumpsFirstSuitComparator cmp;

    @Before
    public void setUp() throws Exception {
        cmp = new TrumpsFirstSuitComparator(Suit.SPADES);
    }

    @Test
    public void compareShouldPreserveEnumOrder() {
        assertThat(Suit.CLUBS).usingComparator(cmp)
                .isLessThan(Suit.DIAMONDS)
                .isLessThan(Suit.HEARTS);
    }

    @Test
    public void trumpsTrump() {
        assertThat(Suit.CLUBS).usingComparator(cmp).isLessThan(Suit.SPADES);
        assertThat(Suit.HEARTS).usingComparator(cmp).isLessThan(Suit.SPADES);
        assertThat(Suit.DIAMONDS).usingComparator(cmp).isLessThan(Suit.SPADES);
    }
}