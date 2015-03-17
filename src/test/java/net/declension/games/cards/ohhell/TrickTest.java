package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.ohhell.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static net.declension.games.cards.CardTest.ACE_OF_SPADES;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class TrickTest {
    private static final Collection<Player> KEYS = asList(DANIELLE, ALICE, BOB);
    private Trick trick;

    @Before
    public void setUp() throws Exception {
        trick = new Trick(KEYS);
    }

    @Test
    public void setCardOrderingShouldBeUsedDuringWinningPlayer() {
        Comparator<Card> mock = mock(Comparator.class);
        trick.setCardOrdering(mock);
        trick.winningPlayer();
        // 2 *sounds* about right
        verify(mock, times(2)).compare(eq(null), eq(null));
    }

    @Test
    public void putAllShouldThrow() {
        assertThatThrownBy(() -> trick.putAll(new HashMap<>())).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void putShouldWork() {
        trick.put(DANIELLE, ACE_OF_SPADES);
        assertThat(trick.get(DANIELLE)).isEqualTo(ACE_OF_SPADES);
        assertThat(trick).hasSize(1);
        assertThat(trick.get(ALICE)).isNull();
    }
}