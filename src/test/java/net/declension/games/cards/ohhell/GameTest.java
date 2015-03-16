package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.sorting.TrumpsAwareSuitsFirstComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {

    public static final int NUM_PLAYERS = 3;
    public static final short HAND_SIZE = 4;
    public static final Suit TEST_TRUMPS = Suit.DIAMONDS;
    public static final Comparator<Card> NORMAL_ORDERER
            = new TrumpsAwareSuitsFirstComparator(new AceHighRankComparator(), TEST_TRUMPS);
    private GameSetup gameSetup;
    private SimpleRandomOhHellStrategy strategy;

    @Before
    public void setUp() throws Exception {
        gameSetup = new GameSetup();
        strategy = new SimpleRandomOhHellStrategy();
    }

    @Test
    public void testPlayRound() {
        List<Player> players = generatePlayers(NUM_PLAYERS);
        Game game = new Game(players);

        // Go
        game.playRound(HAND_SIZE);

        Set<Card> playedCards = new TreeSet<>(NORMAL_ORDERER);
        for (Player player: players) {
            Set<Card> cards = player.hand();
            assertThat(cards).hasSize(HAND_SIZE);
            assertThat(playedCards).doesNotContainAnyElementsOf(cards);
        }
        assertThat(playedCards);
    }

    private List<Player> generatePlayers(int numPlayers) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(strategy, gameSetup))
                .collect(toList());
    }

}