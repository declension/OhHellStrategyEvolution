package net.declension.games.cards.ohhell;

import net.declension.ea.cards.ohhell.Player;
import net.declension.games.cards.sorting.AceHighRankComparator;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.TrumpsAwareSuitsFirstComparator;
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
    public static final int HAND_SIZE = 4;
    public static final Suit TEST_TRUMPS = Suit.DIAMONDS;
    public static final Comparator<Card> NORMAL_ORDERER
            = new TrumpsAwareSuitsFirstComparator(TEST_TRUMPS, new AceHighRankComparator());

    @Test
    public void testPlayRound() {
        List<Player> players = generatePlayers(NUM_PLAYERS);
        Game game = new Game(players);
        game.playRound(HAND_SIZE);

        Set<Card> playedCards = new TreeSet<>(NORMAL_ORDERER);
        for (Player player: players) {
            Set<Card> cards = game.getCardsForPlayers().get(player);
            assertThat(cards).hasSize(HAND_SIZE);
            assertThat(playedCards).doesNotContainAnyElementsOf(cards);
            playedCards.addAll(cards);
        }
    }

    private List<Player> generatePlayers(int numPlayers) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new Player())
                .collect(toList());
    }

}