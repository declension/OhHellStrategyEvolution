package net.declension.games.cards.ohhell;

import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AllBidsTest {
    static final PlayerID ALICE = new PlayerID("Alice");
    static final PlayerID BOB = new PlayerID("Bob");
    static final PlayerID CHARLIE = new PlayerID("Charlie");
    static final PlayerID DANIELLE = new PlayerID("Danielle");
    static final Collection<PlayerID> NAMES = asList(ALICE, BOB, CHARLIE, DANIELLE);
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
        HashMap<PlayerID, Integer> map = new HashMap<PlayerID, Integer>() {{
            put(ALICE, 3);
            put(BOB, 2);
            put(CHARLIE, 1);}};

        bids = new AllBids(NAMES);
        bids.putAll(map);
    }
}