package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.sorting.TrumpsSuitThenRankCardComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

import static java.util.Arrays.asList;
import static net.declension.games.cards.Suit.DIAMONDS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class TrickTest {
    private static final Collection<Player> KEYS = asList(DANIELLE, ALICE, BOB);
    public static final Optional<Suit> TRUMPS = Optional.of(DIAMONDS);
    private Trick trick;

    @Before
    public void setUp() {
        trick = new Trick(KEYS, TRUMPS);
    }

    @Test
    public void winningPlayerShouldNotUseComparatorForEmptyHands() {
        Comparator<Card> mock = mock(Comparator.class);
        trick.setCardOrdering(mock);
        Optional<Player> value = trick.winningPlayer();
        // 2 *sounds* about right
        verify(mock, never()).compare(any(), any());
        assertThat(value.isPresent()).isFalse();
    }

    @Test
    public void winningPlayerShouldUseComparator() {
        Comparator rankComparator = mock(Comparator.class);
        trick.setCardOrdering(new TrumpsSuitThenRankCardComparator(rankComparator, TRUMPS));
        trick.put(DANIELLE, ACE_OF_HEARTS);
        trick.put(BOB, TWO_OF_DIAMONDS);
        Optional<Player> value = trick.winningPlayer();
        // 2 *sounds* about right
        verify(rankComparator, never()).compare(any(), any());
        assertThat(value.get()).isEqualTo(BOB);
    }


    @Test
    public void putAllShouldThrow() {
        assertThatThrownBy(() -> trick.putAll(new HashMap<>())).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void putShouldWork() {
        trick.put(DANIELLE, Optional.of(ACE_OF_SPADES));
        assertThat(trick.get(DANIELLE).get()).isEqualTo(ACE_OF_SPADES);
        assertThat(trick).hasSize(1);
        assertThat(trick.get(ALICE).isPresent()).isFalse();
    }
}