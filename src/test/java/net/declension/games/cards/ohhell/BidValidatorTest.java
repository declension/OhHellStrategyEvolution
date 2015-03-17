package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.DummyPlayer;
import net.declension.games.cards.ohhell.player.Player;
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
        List<Player> players = generatePlayers(NUM_PLAYERS);
        Set<Integer> allowedBids = ALWAYS_FAIL.getAllowedBidsForPlayer(
                players.get(0), HAND_SIZE, new AllBids(players));
        assertThat(allowedBids).isEmpty();
    }

    @Test
    public void getAllowedBidsForPlayerShouldBeFullForAlwaysPassingValidator() {
        List<Player> players = generatePlayers(NUM_PLAYERS);
        Set<Integer> allowedBids = ALWAYS_PASS.getAllowedBidsForPlayer(players.get(0), HAND_SIZE, new AllBids((List<Player>) players));
        assertThat(allowedBids).hasSize(HAND_SIZE + 1);
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


    public static List<Player> generatePlayers(int numPlayers) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new DummyPlayer())
                .collect(toList());
    }

}