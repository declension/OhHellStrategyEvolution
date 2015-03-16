package net.declension.games.cards.ohhell;

import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BidValidatorTest {

    private static final int NUM_PLAYERS = 3;
    private static final Integer HAND_SIZE = 5;


    @Test
    public void getAllowedBidsForPlayerShouldBeEmptyForAlwaysFailingValidator() {
        List<PlayerID> playerIDs = generatePlayerIDs(NUM_PLAYERS);
        Set<Integer> allowedBids = ALWAYS_FAIL.getAllowedBidsForPlayer(
                playerIDs.get(0), HAND_SIZE, generateAllBids(playerIDs));
        assertThat(allowedBids).isEmpty();
    }

    @Test
    public void getAllowedBidsForPlayerShouldBeFullForAlwaysPassingValidator() {
        List<PlayerID> playerIDs = generatePlayerIDs(NUM_PLAYERS);
        Set<Integer> allowedBids = ALWAYS_PASS.getAllowedBidsForPlayer(playerIDs.get(0), HAND_SIZE, generateAllBids(playerIDs));
        assertThat(allowedBids).hasSize(HAND_SIZE + 1);
    }


    private AllBids generateAllBids(List<PlayerID> playerIDs) {
        return new AllBids(playerIDs);
    }

    static final HardCodedBidValidator ALWAYS_FAIL = new HardCodedBidValidator(false);
    static final HardCodedBidValidator ALWAYS_PASS = new HardCodedBidValidator(true);
    static class HardCodedBidValidator implements BidValidator{
        private final boolean answer;

        private HardCodedBidValidator(boolean answer) {
            this.answer = answer;
        }

        @Override
        public boolean test(AllBids allBids) {
            return answer;
        }
    }


    public static List<PlayerID> generatePlayerIDs(int numPlayers) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new PlayerID())
                .collect(toList());
    }
}