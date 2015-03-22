package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static net.declension.games.cards.Suit.SPADES;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsHighDisplaySuitComparatorTest {

    private TrumpsHighDisplaySuitComparator cmp;

    @Before
    public void setUp() throws Exception {
        cmp = new TrumpsHighDisplaySuitComparator(Optional.of(SPADES));
    }

    @Test
    public void compareShouldPreserveEnumOrder() {
        assertThat(Suit.CLUBS).usingComparator(cmp)
                .isLessThan(Suit.DIAMONDS)
                .isLessThan(Suit.HEARTS);
    }

    @Test
    public void trumpsShouldTrump() {
        assertThat(Suit.CLUBS).usingComparator(cmp).isLessThan(SPADES);
        assertThat(Suit.HEARTS).usingComparator(cmp).isLessThan(SPADES);
        assertThat(Suit.DIAMONDS).usingComparator(cmp).isLessThan(SPADES);
    }
}