package net.declension.games.cards;

import net.declension.collections.EnumComparator;
import net.declension.games.cards.sorting.TrumpsHighDisplayComparator;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static net.declension.games.cards.Suit.HEARTS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {

    public static final int EXPECTED_DECK_SIZE = Suit.ALL_SUITS.size() * Rank.ALL_RANKS.size();

    @Test
    public void rankShouldWork() {
        assertThat(ACE_OF_SPADES.rank().equals(Rank.ACE));
        assertThat(TWO_OF_CLUBS.rank().equals(Rank.TWO));
    }

    @Test
    public void suitShouldWork() {
        assertThat(ACE_OF_SPADES.suit().equals(Suit.SPADES));
        assertThat(TWO_OF_CLUBS.suit().equals(Suit.CLUBS));
    }

    @Test
    public void prettyStringShouldContainSymbols() {
        assertThat(ACE_OF_SPADES.prettyString()).contains("A").contains("♠");
    }

    @Test
    public void toStringShouldContainSymbols() {
        assertThat(ACE_OF_SPADES.prettyString()).contains("A").contains("♠");
    }

    @Test
    public void equalsShouldWorkThoroughly() {
        assertThat(ACE_OF_SPADES).isEqualTo(ACE_OF_SPADES);
        assertThat(ACE_OF_SPADES).isNotEqualTo(ACE_OF_HEARTS);
        assertThat(TWO_OF_CLUBS).isEqualTo(TWO_OF_CLUBS);
        assertThat(TWO_OF_DIAMONDS).isNotEqualTo(TWO_OF_CLUBS);
    }

    @Test
    public void equalsShouldWorkForDumbCases() {
        assertThat(ACE_OF_SPADES).isNotEqualTo(null);
        assertThat(ACE_OF_SPADES).isNotEqualTo(99);
    }

    @Test
    public void hashCodeShould() {
        assertThat(ACE_OF_SPADES.hashCode()).isEqualTo(ACE_OF_SPADES.hashCode());
        assertThat(ACE_OF_SPADES.hashCode()).isNotEqualTo(ACE_OF_HEARTS.hashCode());
        assertThat(TWO_OF_CLUBS.hashCode()).isEqualTo(TWO_OF_CLUBS.hashCode());
        assertThat(TWO_OF_DIAMONDS.hashCode()).isNotEqualTo(TWO_OF_CLUBS.hashCode());
    }

    @Test
    public void allCardsShouldHaveRightTotal() {
        List<Card> all = Card.allPossibleCards();
        assertThat(all).hasSize(EXPECTED_DECK_SIZE);
        // Check for duplicates
        assertThat(new HashSet<>(all)).hasSize(EXPECTED_DECK_SIZE);
    }

    @Test
    public void beatsShouldWork() {
        assertThat(ACE_OF_HEARTS.beats(TWO_OF_CLUBS).using((l,r) -> 1)).isTrue();
        assertThat(ACE_OF_HEARTS.beats(TWO_OF_CLUBS).using((l,r) -> -1)).isFalse();
        Comparator<Card> cmp = new TrumpsHighDisplayComparator(new EnumComparator<>(), Optional.of(HEARTS));
        assertThat(TWO_OF_DIAMONDS.beats(TWO_OF_CLUBS).using(cmp)).isFalse();
    }
}