package net.declension.games.cards;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CardTest {
    public static final Card ACE_OF_SPADES = new Card(Rank.ACE, Suit.SPADES);
    public static final Card ACE_OF_HEARTS = new Card(Rank.ACE, Suit.HEARTS);
    public static final Card TWO_OF_DIAMONDS = new Card(Rank.TWO, Suit.DIAMONDS);
    public static final Card TWO_OF_CLUBS = new Card(Rank.TWO, Suit.CLUBS);

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
}