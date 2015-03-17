package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
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
import java.util.Set;
import java.util.TreeSet;
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
    private List<Player> players;

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
        players.forEach(player ->
                assertThat(player.hand()).hasSize(MAX_HAND_SIZE));
    }

    @Test
    public void testPlayRound() {

        // Go
        game.playRound(HAND_SIZE);

        Set<Card> playedCards = new TreeSet<>(NORMAL_ORDERER);
        for (Player player: players) {
            Set<Card> cards = player.hand();
            assertThat(cards).hasSize(0);
            assertThat(playedCards).doesNotContainAnyElementsOf(cards);
        }
        assertThat(playedCards);
    }

    public static List<Player> generatePlayers(int numPlayers, OhHellStrategy strategy, GameSetup gameSetup) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(strategy, gameSetup))
                .collect(toList());
    }

}