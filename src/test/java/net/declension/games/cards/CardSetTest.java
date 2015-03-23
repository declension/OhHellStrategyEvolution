package net.declension.games.cards;

import net.declension.games.cards.sorting.Comparators;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Arrays.asList;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CardSetTest {

    private static final Collection<Card> CARDS = asList(QUEEN_OF_DIAMONDS, TWO_OF_DIAMONDS, TWO_OF_CLUBS,
                                                         ACE_OF_HEARTS);
    public static final Comparator<Card> DIAMONDS_TRUMPS
            = Comparators.standardComparator(Optional.of(Suit.DIAMONDS), Optional.empty());
    private CardSet cards;

    @Before
    public void setUp() {
        cards = new CardSet(DIAMONDS_TRUMPS, CARDS);
    }

    @Test
    public void sizeShouldWork() {
        assertThat(cards).hasSize(CARDS.size());
    }

    @Test
    public void getHighestHappyPath() {
        assertThat(cards.getHighest()).isEqualTo(QUEEN_OF_DIAMONDS);
    }

    @Test
    public void getLowestHappyPath() {
        assertThat(cards.getLowest()).isEqualTo(TWO_OF_CLUBS);
    }

    @Test
    public void standardConstructorShouldInitialiseEmptySet() {
        assertThat(new CardSet(DIAMONDS_TRUMPS)).isEmpty();
    }
}