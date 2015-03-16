package net.declension.games.cards.ohhell;

import com.google.common.collect.ImmutableSet;
import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static net.declension.Utils.requireNonNullParam;

public class BasicPlayer implements Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final PlayerID playerID;
    private OhHellStrategy strategy;
    private CardSet hand;
    private Suit trumps;


    public BasicPlayer(OhHellStrategy strategy) {
        this(new PlayerID(), strategy);
    }

    public BasicPlayer(PlayerID playerID, OhHellStrategy strategy) {
        requireNonNullParam(playerID, "Player ID");
        requireNonNullParam(strategy, "Game strategy");
        this.strategy = strategy;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return String.format("Player %s", playerID);
    }


    @Override
    public void receiveNewHand(Suit trumps, Collection<Card> cards) {
        this.trumps = trumps;
        hand = new CardSet(trumps);
        hand.addAll(cards);
    }

    @Override
    public Integer bid(Game game, AllBids bidsSoFar) {
        Set<Integer> allowedBids = game.getBidValidator().getAllowedBidsForPlayer(playerID, hand.size(), bidsSoFar);
        Integer bid = strategy.chooseBid(trumps, hand, bidsSoFar, allowedBids);
        LOGGER.info("{} is bidding {} using {}", this, bid, strategy);
        return bid;
    }

    @Override
    public Card playCard(Game game, Map<PlayerID, BidAndTaken> bidAndTakens, SlotsMap<PlayerID, Card> trickSoFar) {
        return null;
    }

    @Override
    public Set<Card> hand() {
        return ImmutableSet.copyOf(hand);
    }

    @Override
    public PlayerID getID() {
        return playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicPlayer)) {
            return false;
        }
        BasicPlayer other = (BasicPlayer) o;
        return playerID.equals(other.playerID);

    }

    @Override
    public int hashCode() {
        return playerID.hashCode();
    }

}
