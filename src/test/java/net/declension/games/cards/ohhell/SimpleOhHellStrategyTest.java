package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static net.declension.games.cards.ohhell.BidValidatorTest.generatePlayerIDs;
import static org.assertj.core.api.Assertions.assertThat;

public class SimpleOhHellStrategyTest {

    private static final int NUM_CARDS = 5;
    public static final Suit TEST_TRUMPS = Suit.HEARTS;
    public static final int NUM_PLAYERS = 3;
    private OhHellStrategy strategy;
    private BidValidator bidValidator;

    @Before
    public void setUp() throws Exception {
        strategy = new SimpleOhHellStrategy();
        bidValidator = new BidBustingRulesBidValidator(NUM_CARDS);
    }

    @Test
    public void chooseBidShouldBeAllowedAndAverageForFirstPlayer() {
        List<PlayerID> playerIDs = generatePlayerIDs(NUM_PLAYERS);
        AllBids bidsSoFar = new AllBids(playerIDs);

        Set<Integer> allowedBids = bidValidator.getAllowedBidsForPlayer(playerIDs.get(2), NUM_CARDS, bidsSoFar);
        Set<Card> cards = new CardSet(TEST_TRUMPS, new Deck().shuffled().pullCards(NUM_CARDS));
        Integer bid = strategy.chooseBid(TEST_TRUMPS, cards, bidsSoFar, allowedBids);
        assertThat(bid).isIn(allowedBids);
        assertThat(bid).isEqualTo(NUM_CARDS / NUM_PLAYERS);
    }

}