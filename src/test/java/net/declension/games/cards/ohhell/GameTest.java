package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import net.declension.games.cards.ohhell.strategy.SimpleOhHellStrategy;
import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.TrumpsFirstSuitComparator;
import org.junit.Before;
import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    public static final int NUM_PLAYERS = 4;
    public static final int HAND_SIZE = 4;
    public static final Suit TEST_TRUMPS = Suit.DIAMONDS;
    public static final Comparator<Card> NORMAL_ORDERER
            = new SuitThenRankComparator(new AceHighRankComparator(), new TrumpsFirstSuitComparator(TEST_TRUMPS));
    public static final int MAX_HAND_SIZE = 7;
    private GameSetup gameSetup;
    private Game game;

    // We need this to be BasicPlayer to allow a little cheating in the test..
    private List<BasicPlayer> players;

    @Before
    public void setUp() {
        gameSetup = new GameSetup(IntStream.rangeClosed(1, MAX_HAND_SIZE).boxed());
        players = generatePlayers(NUM_PLAYERS,
                new SimpleOhHellStrategy(new MersenneTwisterRNG()), gameSetup);
        game = new Game(players, gameSetup, players.get(0));
    }

    @Test
    public void playShouldWork() {
        game.play();
        assertThatNobodyHasCards();
    }

    private void assertThatNobodyHasCards() {
        players.forEach(player -> assertThat(player.peekAtHand()).isEmpty());
    }

    @Test
    public void testPlayRound() {
        game.playRound(HAND_SIZE);
        assertThatNobodyHasCards();
    }

    public static List<BasicPlayer> generatePlayers(int numPlayers, OhHellStrategy strategy, GameSetup gameSetup) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(strategy, gameSetup))
                .collect(toList());
    }

}