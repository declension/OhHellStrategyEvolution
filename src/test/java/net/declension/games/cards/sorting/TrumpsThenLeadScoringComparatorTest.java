package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;

import static net.declension.games.cards.Suit.CLUBS;
import static net.declension.games.cards.Suit.HEARTS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsThenLeadScoringComparatorTest {

    @Test
    public void compareShouldCareAboutTrumpsAndLeads() {
        Comparator<Card> cmp = new TrumpsThenLeadScoringComparator(new AceHighRankComparator(),
                                                                   Optional.of(HEARTS), Optional.of(CLUBS));
        assertThat(cmp.compare(ACE_OF_HEARTS, TWO_OF_CLUBS)).isEqualTo(1);
        assertThat(cmp.compare(TWO_OF_CLUBS, ACE_OF_SPADES)).isEqualTo(1);
        assertThat(cmp.compare(JACK_OF_CLUBS, TWO_OF_CLUBS)).isEqualTo(1);
        assertThat(cmp.compare(ACE_OF_SPADES, TWO_OF_DIAMONDS)).isEqualTo(0);
    }

    @Test
    public void compareShouldCareAboutLeadsOnlyWhenNoTrumps() {
        Comparator<Card> cmp = new TrumpsThenLeadScoringComparator(new AceHighRankComparator(),
                                                                   Optional.empty(), Optional.of(CLUBS));
        assertThat(cmp.compare(ACE_OF_HEARTS, TWO_OF_CLUBS)).isEqualTo(-1);
        assertThat(cmp.compare(TWO_OF_CLUBS, ACE_OF_SPADES)).isEqualTo(1);
        assertThat(cmp.compare(JACK_OF_CLUBS, TWO_OF_CLUBS)).isEqualTo(1);
        assertThat(cmp.compare(ACE_OF_SPADES, TWO_OF_DIAMONDS)).isEqualTo(0);
    }
}
