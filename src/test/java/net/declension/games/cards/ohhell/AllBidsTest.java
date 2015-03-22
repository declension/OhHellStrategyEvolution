package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.Player;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.declension.games.cards.ohhell.player.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AllBidsTest {
    private AllBids bids;

    @Before
    public void setUp() {
        createBids();
    }

    @Test
    public void copyWithBidShouldIncludeNewBid() {
        assertThat(bids).hasSize(3).doesNotContain(MapEntry.entry(DANIELLE, Optional.of(1)));
        assertThat(bids.copyWithBid(DANIELLE, 1)).hasSize(4).contains(MapEntry.entry(DANIELLE, Optional.of(1)));
    }

    @Test
    public void getFinalBidsShouldThrowForUnfinished() {
        assertThatThrownBy(bids::getFinalBids).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void getFinalBidsShouldReturnAllIntact() {
        bids.put(DANIELLE, Optional.of(0));
        Map<Player, Integer> finalBids = bids.getFinalBids();
        assertThat(finalBids).containsKey(DANIELLE);
        assertThat(finalBids.keySet()).containsSequence(ALICE, BOB, CHARLIE, DANIELLE);
    }

    private void createBids() {
        HashMap<Player, Optional<Integer>> map = new HashMap<Player, Optional<Integer>>() {{
            put(BOB, Optional.of(2));
            put(ALICE, Optional.of(3));
            put(CHARLIE, Optional.of(1));
        }};

        bids = new AllBids(PLAYERS);
        bids.putAll(map);
    }
}