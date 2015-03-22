package net.declension.games.cards.sorting.suit;

import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Rank;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static net.declension.games.cards.Suit.CLUBS;
import static net.declension.games.cards.Suit.HEARTS;
import static org.assertj.core.api.Assertions.assertThat;

public class SuitThenRankComparatorTest {

    public static final TrumpsHighDisplaySuitComparator TEST_SUIT_COMPARATOR
            = new TrumpsHighDisplaySuitComparator(Optional.of(HEARTS));

    @Test
    public void compareShouldWorkOnDeck() {
        ArrayList<Card> cards = new ArrayList<>(new Deck().shuffled().cards());
        Collections.sort(cards, new SuitThenRankComparator(TEST_SUIT_COMPARATOR, new AceHighRankComparator()));
        assertThat(cards).extracting("rank").containsSequence(Rank.TWO, Rank.THREE, Rank.FOUR);
        Card first = cards.get(0);
        Card last = cards.get(cards.size() - 1);
        assertThat(first.suit()).isEqualTo(CLUBS);
        assertThat(first.rank()).isEqualTo(Rank.TWO);
        assertThat(last.suit()).isEqualTo(HEARTS);
        assertThat(last.rank()).isEqualTo(Rank.ACE);
    }
}