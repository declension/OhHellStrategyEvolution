package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import static net.declension.games.cards.Suit.HEARTS;
import static net.declension.games.cards.ohhell.player.TestData.ACE_OF_HEARTS;
import static net.declension.games.cards.ohhell.player.TestData.TWO_OF_CLUBS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GameSetupTest {

    private GameSetup setup;

    @Before
    public void setUp() throws Exception {
        setup = new GameSetup(mock(Supplier.class), mock(OhHellRules.class));
    }

    @Test
    public void createTrickComparatorShould() {
        Comparator<Card> cmp = setup.createDisplayComparator(Optional.of(HEARTS));
        assertThat(ACE_OF_HEARTS.beats(TWO_OF_CLUBS).using(cmp)).isTrue();
    }
}