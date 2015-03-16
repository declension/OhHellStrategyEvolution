package net.declension.games.cards.sorting;

import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class TrumpsAwareSuitsFirstComparatorTest {

    @Test
    public void compareShouldWorkOnDeck() {
        ArrayList<Card> cards = new ArrayList<>(new Deck().shuffled().cards());
        Collections.sort(cards, new TrumpsAwareSuitsFirstComparator(new AceHighRankComparator(), Suit.HEARTS));
        assertThat(cards).extracting("rank").containsSequence(Rank.TWO, Rank.THREE, Rank.FOUR);
        Card first = cards.get(0);
        Card last = cards.get(cards.size() - 1);
        assertThat(first.suit()).isEqualTo(Suit.CLUBS);
        assertThat(first.rank()).isEqualTo(Rank.TWO);
        assertThat(last.suit()).isEqualTo(Suit.HEARTS);
        assertThat(last.rank()).isEqualTo(Rank.ACE);
    }
}