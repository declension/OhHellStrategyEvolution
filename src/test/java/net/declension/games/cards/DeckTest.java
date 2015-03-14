package net.declension.games.cards;

import org.junit.Before;
import org.junit.Test;

import static net.declension.games.cards.Rank.ACE;
import static net.declension.games.cards.Suit.SPADES;
import static org.assertj.core.api.Assertions.assertThat;


public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void getAllCardsShouldReturn52() {
        assertThat(deck).hasSize(52);
    }

    @Test
    public void allCardsShouldContainSomeKeyCards() {
        assertThat(new Card(ACE, SPADES)).isIn(deck.cards());
    }

    @Test
    public void cardsFromNewDecksShouldBeEqual() {
        Deck anotherDeck = new Deck();
        // Sanity check...
        assertThat(deck.cards()).containsAll(anotherDeck.cards());
    }

    @Test
    public void shuffleShouldNotBeInOrder() {
        // Technically, it *could* be, I guess...
        assertThat(deck.shuffled()).isNotSameAs(deck);
    }
}