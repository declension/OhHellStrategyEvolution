package net.declension.games.cards.sorting;

import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static net.declension.games.cards.Suit.DIAMONDS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsHighDisplayComparatorTest {

    private static final Optional<Suit> TRUMPS = Optional.of(DIAMONDS);
    private TrumpsHighDisplayComparator cmp;

    @Before
    public void setUp() throws Exception {
        cmp = new TrumpsHighDisplayComparator(new AceHighRankComparator(), TRUMPS);
    }

    @Test
    public void trumpsShouldBeatHighRank() {
        assertThat(TRUMPS.get()).isEqualTo(DIAMONDS);
        assertThat(cmp.compare(TWO_OF_DIAMONDS, ACE_OF_HEARTS)).isEqualTo(1);
    }

    @Test
    public void highTrumpsShouldBeatLowTrump() {
        assertThat(TRUMPS.get()).isEqualTo(DIAMONDS);
        assertThat(cmp.compare(QUEEN_OF_DIAMONDS, TWO_OF_DIAMONDS)).isEqualTo(1);
    }

    @Test
    public void nonTrumpsShouldBeNormal() {
        assertThat(TRUMPS.get()).isEqualTo(DIAMONDS);
        assertThat(cmp.compare(QUEEN_OF_DIAMONDS, TWO_OF_DIAMONDS)).isEqualTo(1);
    }
}