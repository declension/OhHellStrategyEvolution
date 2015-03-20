package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Rank;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static net.declension.games.cards.Suit.CLUBS;
import static net.declension.games.cards.Suit.HEARTS;
import static org.assertj.core.api.Assertions.assertThat;

public class SuitThenRankComparatorTest {

    public static final TrumpsFirstSuitComparator TEST_SUIT_COMPARATOR = new TrumpsFirstSuitComparator(HEARTS);

    @Test
    public void compareShouldWorkOnDeck() {
        ArrayList<Card> cards = new ArrayList<>(new Deck().shuffled().cards());
        Collections.sort(cards, new SuitThenRankComparator(new AceHighRankComparator(), TEST_SUIT_COMPARATOR));
        assertThat(cards).extracting("rank").containsSequence(Rank.TWO, Rank.THREE, Rank.FOUR);
        Card first = cards.get(0);
        Card last = cards.get(cards.size() - 1);
        assertThat(first.suit()).isEqualTo(CLUBS);
        assertThat(first.rank()).isEqualTo(Rank.TWO);
        assertThat(last.suit()).isEqualTo(HEARTS);
        assertThat(last.rank()).isEqualTo(Rank.ACE);
    }
}