package net.declension.games.cards.ohhell;

import net.declension.ea.cards.ohhell.Player;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.iterableWithSize;

public class GameTest {

    public static final int NUM_PLAYERS = 4;
    public static final int HAND_SIZE = 5;

    @Test
    public void testPlayRound() {
        List<Player> players = generatePlayers(NUM_PLAYERS);
        Game game = new Game(players);
        game.playRound(HAND_SIZE);
        for (Player player: players) {
            assertThat(game.getCardsForPlayers().get(player), iterableWithSize(HAND_SIZE));
        }
    }

    private List<Player> generatePlayers(int numPlayers) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new Player())
                .collect(toList());
    }

}