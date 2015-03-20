package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import org.junit.Before;
import org.junit.Test;

import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsSuitThenRankCardComparatorTest {

    private static final Suit TRUMPS = Suit.DIAMONDS;
    private TrumpsSuitThenRankCardComparator cmp;

    @Before
    public void setUp() throws Exception {
        cmp = new TrumpsSuitThenRankCardComparator(new AceHighRankComparator(), TRUMPS);
    }

    @Test
    public void trumpsShouldBeatHighRank() {
        assertThat(TRUMPS).isEqualTo(Suit.DIAMONDS);
        assertThat(cmp.compare(TWO_OF_DIAMONDS, ACE_OF_HEARTS)).isEqualTo(1);
    }

    @Test
    public void highTrumpsShouldBeatLowTrump() {
        assertThat(TRUMPS).isEqualTo(Suit.DIAMONDS);
        assertThat(cmp.compare(QUEEN_OF_DIAMONDS, TWO_OF_DIAMONDS)).isEqualTo(1);
    }

    @Test
    public void nonTrumpsShouldBeNormal() {
        assertThat(TRUMPS).isEqualTo(Suit.DIAMONDS);
        assertThat(cmp.compare(QUEEN_OF_DIAMONDS, TWO_OF_DIAMONDS)).isEqualTo(1);
    }
}