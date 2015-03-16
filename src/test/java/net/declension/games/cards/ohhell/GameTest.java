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
    private GameSetup gameSetup;

    @Before
    public void setUp() {
        gameSetup = new GameSetup();
    }

    @Test
    public void testPlayRound() {
        List<Player> players = generatePlayers(NUM_PLAYERS, new SimpleOhHellStrategy(new MersenneTwisterRNG()), gameSetup);
        Game game = new Game(players, gameSetup, players.get(0).getID());

        // Go
        game.playRound(HAND_SIZE);

        Set<Card> playedCards = new TreeSet<>(NORMAL_ORDERER);
        for (Player player: players) {
            Set<Card> cards = player.hand();
            assertThat(cards).hasSize(HAND_SIZE - 1);
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