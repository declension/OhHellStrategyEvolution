package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.DummyPlayer;
import net.declension.games.cards.ohhell.player.Player;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class BidBustingRulesBidValidatorTest {

    @Test
    public void testFullRounds() {
        BidValidator validator = new BidBustingRulesBidValidator(3);
        assertThat(validator.test(createBids(2, 2, 2))).isTrue();
        assertThat(validator.test(createBids(0, 0, 0))).isTrue();
        assertThat(validator.test(createBids(1, 1, 1))).isFalse();
        assertThat(validator.test(createBids(3, 0, 0))).isFalse();
    }

    @Test
    public void unfinishedRoundsShouldBeValid() {
        BidValidator validator = new BidBustingRulesBidValidator(3);
        assertThat(validator.test(createBids(2, 2, null))).isTrue();
        assertThat(validator.test(createBids(null, null, null))).isTrue();
    }

    private static AllBids createBids(Integer... bids) {
        Map<Player, Optional<Integer>> bidsMap = new HashMap<>();
        for (Integer bid: bids) {
            bidsMap.put(new DummyPlayer(), Optional.ofNullable(bid));
        }
        AllBids ret = new AllBids(bidsMap.keySet());
        ret.putAll(bidsMap);
        return ret;
    }
}