package net.declension.games.cards.ohhell.strategy;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.FirstCardListener;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static net.declension.games.cards.Suit.DIAMONDS;
import static net.declension.games.cards.ohhell.player.TestData.*;
import static net.declension.games.cards.sorting.Comparators.standardComparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class SimpleStrategyTest {
    private static final Optional<Suit> TRUMPS = Optional.of(DIAMONDS);
    private static final Set<Card> A_HAND = new HashSet<>(asList(TWO_OF_CLUBS, ACE_OF_HEARTS, QUEEN_OF_DIAMONDS));
    private SimpleStrategy strategy;
    private Set<Card> allowedCards;
    Player player;
    private List<Player> players;
    private AllBids allBids;
    private Map<Player, Integer> finalBids;
    private Trick trick;

    @Before
    public void setUp() {
        GameSetup gameSetup = new GameSetup(mock(Supplier.class));
        strategy = new SimpleStrategy(gameSetup);
        player = new BasicPlayer(strategy, gameSetup);
        players = asList(ALICE, BOB, player);
        allBids = new AllBids(players);
        FirstCardListener<Trick> listener
                = (t, c) -> t.setCardOrdering(standardComparator(TRUMPS, Optional.of(c.suit())));
        finalBids = new HashMap<>();
        trick = new Trick(players, listener);
    }

    @Test
    public void chooseBidShouldNotThrow() {
        Set<Integer> allowedBids = new HashSet<>(asList(1,2,3,4));
        Integer bid = strategy.chooseBid(TRUMPS, player, A_HAND, allBids, allowedBids);
        assertThat(bid).isIn(allowedBids);
        assertThat(bid).isEqualTo(A_HAND.size() / players.size());
    }

    @Test
    public void chooseFollowingCardShouldPlayTrumpsIfBelowBid() {

        trick.put(players.get(0), ACE_OF_SPADES);
        // Any card will do...
        finalBids.put(player, 1);

        allowedCards = new HashSet<>(A_HAND);

        Card card = strategy.chooseFollowingCard(TRUMPS, player, A_HAND, finalBids,
                                                 new SlotsMap<>(players, 0), trick, allowedCards);

        assertThat(card).isIn(allowedCards);
        assertThat(card).isEqualTo(QUEEN_OF_DIAMONDS);
    }

    @Test
    public void chooseLeadingCardShouldPlayHighIfBelowBid() {
        // Any card will do...
        finalBids.put(player, 1);

        Card card = strategy.chooseLeadingCard(TRUMPS, player, A_HAND, finalBids,
                                               new SlotsMap<>(players, 0), A_HAND);

        assertThat(card).isEqualTo(QUEEN_OF_DIAMONDS);
    }
}