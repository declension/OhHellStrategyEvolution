package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.StandardOhHellRoundSizer;
import net.declension.games.cards.ohhell.StandardRules;
import net.declension.games.cards.ohhell.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static net.declension.games.cards.ohhell.player.TestData.ACE_OF_SPADES;
import static net.declension.games.cards.ohhell.player.TestData.JACK_OF_CLUBS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TrumpsBasedGeneticRandomStrategyIntegrationTest {

    private static final Optional<Suit> TRUMPS = Optional.of(Suit.SPADES);
    private TrumpsBasedGeneticRandomStrategy strategy;
    private GameSetup gameSetup;


    @Before
    public void setUp() throws Exception {
        gameSetup = new GameSetup(new StandardOhHellRoundSizer(), new StandardRules());
        strategy = new TrumpsBasedGeneticRandomStrategy(gameSetup);
    }

    @Test
    public void evaluateShouldFavourTrumpVersusNonTrumpForSingleCard() {
        Player me = mock(Player.class);
        Collection<? extends Player> players = asList(me, mock(Player.class));
        AllBids bidsSoFar = new AllBids(players);
        Set<Integer> allowedBids = new HashSet<>(asList(0, 1));

        Set<Card> goodCards = new HashSet<>(singletonList(ACE_OF_SPADES));
        assertThat(strategy.chooseBid(TRUMPS, me, goodCards, bidsSoFar, allowedBids))
                .isEqualTo(1);

        Set<Card> averageCards = new HashSet<>(singletonList(JACK_OF_CLUBS));
        assertThat(strategy.chooseBid(TRUMPS, me, averageCards, bidsSoFar, allowedBids))
                .isEqualTo(0);
    }
}
