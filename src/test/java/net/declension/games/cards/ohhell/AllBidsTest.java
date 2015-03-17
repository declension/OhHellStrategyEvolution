package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.DummyPlayer;
import net.declension.games.cards.ohhell.player.Player;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AllBidsTest {
    static final Player ALICE = new DummyPlayer("Alice");
    static final Player BOB = new DummyPlayer("Bob");
    static final Player CHARLIE = new DummyPlayer("Charlie");
    static final Player DANIELLE = new DummyPlayer("Danielle");
    static final Collection<Player> PLAYERS = asList(ALICE, BOB, CHARLIE, DANIELLE);
    private AllBids bids;

    @Before
    public void setUp() {
        createBids();
    }

    @Test
    public void copyWithBidShouldIncludeNewBid() {
        assertThat(bids).hasSize(3).doesNotContain(MapEntry.entry(DANIELLE, 1));
        assertThat(bids.copyWithBid(DANIELLE, 1)).hasSize(4).contains(MapEntry.entry(DANIELLE, 1));
    }

    private void createBids() {
        HashMap<Player, Integer> map = new HashMap<Player, Integer>() {{
            put(ALICE, 3);
            put(BOB, 2);
            put(CHARLIE, 1);}};

        bids = new AllBids(PLAYERS);
        bids.putAll(map);
    }
}