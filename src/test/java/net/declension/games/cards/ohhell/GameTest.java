package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.strategy.SimpleStrategy;
import net.declension.games.cards.ohhell.strategy.Strategy;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    public static final int NUM_PLAYERS = 4;
    public static final int HAND_SIZE = 8;
    public static final int MAX_HAND_SIZE = 8;
    private Game game;

    // We need this to be BasicPlayer to allow a little cheating in the test..
    private List<Player> players;

    @Before
    public void setUp() {
        GameSetup gameSetup = new GameSetup(IntStream.rangeClosed(1, MAX_HAND_SIZE).boxed());
        players = generatePlayers(NUM_PLAYERS, new SimpleStrategy(gameSetup.getRNG()), gameSetup);
        game = new Game(players, gameSetup, players.get(0));
    }

    @Test
    public void playShouldWork() {
        game.play();
        assertThatNobodyHasCards();
    }

    private void assertThatNobodyHasCards() {
        players.forEach(player -> assertThat(((BasicPlayer) player).peekAtHand()).isEmpty());
    }

    @Test
    public void testPlayRound() {
        game.playRound(HAND_SIZE);
        assertThatNobodyHasCards();
        assertThat(totalScores()).isEqualTo(HAND_SIZE);
    }

    private int totalScores() {
        return game.getTricksTaken().values().stream().mapToInt(Integer::intValue).sum();
    }

    public static List<Player> generatePlayers(int numPlayers, Strategy strategy, GameSetup gameSetup) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(strategy, gameSetup))
                .collect(toList());
    }

}