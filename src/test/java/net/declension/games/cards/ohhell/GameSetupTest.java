package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Supplier;

import static net.declension.games.cards.Suit.CLUBS;
import static net.declension.games.cards.Suit.HEARTS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GameSetupTest {

    private GameSetup setup;

    @Before
    public void setUp() throws Exception {
        setup = new GameSetup(mock(Supplier.class), mock(OhHellRules.class));
    }

    @Test
    public void createDisplayComparatorShouldOrderBySuit() {
        Comparator<Card> cmp = setup.createDisplayComparator(Optional.of(HEARTS));
        assertThat(ACE_OF_HEARTS.beats(TWO_OF_CLUBS).using(cmp)).isTrue();
        assertThat(TWO_OF_DIAMONDS.beats(JACK_OF_CLUBS).using(cmp)).isTrue();
    }

    @Test
    public void createTrickScoringComparatorShouldCareAboutTrumpsAndLeads() {
        Comparator<Card> cmp = setup.createTrickScoringComparator(Optional.of(HEARTS), Optional.of(CLUBS));
        assertThat(ACE_OF_HEARTS.beats(TWO_OF_CLUBS).using(cmp)).isTrue();
        assertThat(TWO_OF_CLUBS.beats(ACE_OF_SPADES).using(cmp)).isTrue();
        assertThat(JACK_OF_CLUBS.beats(TWO_OF_CLUBS).using(cmp)).isTrue();
        assertThat(cmp.compare(ACE_OF_SPADES, TWO_OF_DIAMONDS)).isEqualTo(0);
    }
}