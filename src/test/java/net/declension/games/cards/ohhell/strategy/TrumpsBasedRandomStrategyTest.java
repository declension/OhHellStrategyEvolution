package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.BidBustingRulesBidValidator;
import net.declension.games.cards.ohhell.BidValidator;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.sorting.SuitThenRankComparator;
import net.declension.games.cards.sorting.rank.AceHighRankComparator;
import net.declension.games.cards.sorting.suit.TrumpsHighDisplaySuitComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static net.declension.games.cards.Suit.HEARTS;
import static net.declension.games.cards.ohhell.BidValidatorTest.generatePlayers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TrumpsBasedRandomStrategyTest {

    private static final int NUM_CARDS = 5;
    public static final Optional<Suit> TEST_TRUMPS = Optional.of(HEARTS);
    public static final Comparator<Card> COMPARATOR
            = new SuitThenRankComparator(new TrumpsHighDisplaySuitComparator(TEST_TRUMPS), new AceHighRankComparator());
    public static final int NUM_PLAYERS = 3;
    public static final int THIS_PLAYER = 2;
    private OhHellStrategy strategy;
    private BidValidator bidValidator;

    @Before
    public void setUp() {
        strategy = new TrumpsBasedRandomStrategy(mock(Random.class));
        bidValidator = new BidBustingRulesBidValidator(NUM_CARDS);
    }

    @Test
    public void chooseBidShouldBeAllowedAndAverageForFirstPlayer() {
        List<Player> players = generatePlayers(NUM_PLAYERS);
        AllBids bidsSoFar = new AllBids(players);

        Set<Integer> allowedBids = bidValidator.getAllowedBidsForPlayer(players.get(THIS_PLAYER), NUM_CARDS, bidsSoFar);
        CardSet cards = new CardSet(COMPARATOR, new Deck().shuffled().pullCards(NUM_CARDS));
        Integer bid = strategy.chooseBid(TEST_TRUMPS, players.get(THIS_PLAYER), cards, bidsSoFar, allowedBids);
        assertThat(bid).isIn(allowedBids);
        //assertThat(bid).isEqualTo(Math.round((float) NUM_CARDS / NUM_PLAYERS));
        // TODO: clever testing of random things
    }

}