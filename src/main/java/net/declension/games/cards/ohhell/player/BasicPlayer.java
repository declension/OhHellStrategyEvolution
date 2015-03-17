package net.declension.games.cards.ohhell.player;

import com.google.common.collect.ImmutableSet;
import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

import static net.declension.Utils.requireNonNullParam;

public class BasicPlayer implements Player {
    private final Logger logger;

    private final PlayerID playerID;
    private final GameSetup gameSetup;
    private OhHellStrategy strategy;
    private CardSet hand;
    private Suit trumps;

    public BasicPlayer(OhHellStrategy strategy, GameSetup gameSetup) {
        this(new PlayerID(), gameSetup, strategy);
    }

    public BasicPlayer(PlayerID playerID, GameSetup gameSetup, OhHellStrategy strategy) {
        requireNonNullParam(playerID, "Player ID");
        requireNonNullParam(strategy, "Game strategy");
        requireNonNullParam(gameSetup, "Game Setup");
        logger = LoggerFactory.getLogger(getClass() + "#" + playerID);
        this.gameSetup = gameSetup;
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
        hand = new CardSet(gameSetup.standardComparatorSupplier().apply(trumps));
        hand.addAll(cards);
    }

    @Override
    public Integer bid(Game game, AllBids bidsSoFar) {
        Set<Integer> allowedBids = game.getBidValidator().getAllowedBidsForPlayer(this, hand.size(), bidsSoFar);
        logger.debug("Bids allowed: {}", allowedBids);
        Integer bid = strategy.chooseBid(trumps, hand, bidsSoFar, allowedBids);
        logger.debug("{} is bidding {} using {}", this, bid, strategy);
        return bid;
    }

    @Override
    public Card playCard(Game game, Trick trickSoFar) {
        Set<Card> allowedCards = hand;
        logger.debug("Hmm, here's my hand: {}", hand);
        Card card = strategy.chooseCard(game.getTrumps(), hand, game.getBidAndTakens(), trickSoFar, allowedCards);
        hand.remove(card);
        return card;
    }

    @Override
    public Set<Card> hand() {
        return ImmutableSet.copyOf(hand);
    }

    @Override
    public OhHellStrategy getStrategy() {
        return strategy;
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
