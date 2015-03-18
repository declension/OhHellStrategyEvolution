package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.Strategy;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseGameTest {
    public static final int MAX_HAND_SIZE = 8;
    protected Game game;
    // We need this to be BasicPlayer to allow a little cheating in the test..
    protected List<Player> players;
    protected GameSetup gameSetup;

    public static List<Player> generatePlayers(int numPlayers, Strategy strategy, GameSetup gameSetup) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(strategy, gameSetup))
                .collect(toList());
    }

    protected int totalScores() {
        return game.getTricksTaken().values().stream().mapToInt(Integer::intValue).sum();
    }

    protected void assertThatNobodyHasCards() {
        players.forEach(player -> assertThat(((BasicPlayer) player).peekAtHand()).isEmpty());
    }

    protected GameSetup createDefaultGameSetup() {
        return new GameSetup(() -> IntStream.rangeClosed(1, BaseGameTest.MAX_HAND_SIZE).boxed());
    }
}
