package net.declension.games.cards.ohhell.player;

import com.google.common.collect.ImmutableSet;
import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Rank;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.BiddingStrategy;
import net.declension.games.cards.ohhell.strategy.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static net.declension.Utils.requireNonNullParam;

public class BasicPlayer implements Player {
    private final Logger logger;

    private final PlayerID playerID;
    private final GameSetup gameSetup;
    private Strategy strategy;
    private CardSet hand;
    private Suit trumps;

    public BasicPlayer(Strategy strategy, GameSetup gameSetup) {
        this(new PlayerID(), gameSetup, strategy);
    }

    public BasicPlayer(PlayerID playerID, GameSetup gameSetup, Strategy strategy) {
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
        hand = new CardSet(gameSetup.createRoundComparator(trumps));
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
        logger.debug("Hmm, here's my hand: {}", hand);
        Set<Card> allowedCards = getAllowedCards(trickSoFar);
        Card card = strategy.chooseCard(game.getTrumps(), hand, game.getTricksBid(), game.getTricksTaken(),
                                        trickSoFar, allowedCards);
        hand.remove(card);
        if (card.rank() == Rank.ACE && card.suit() == trumps) {
            logger.info("Hand 'em over people, trumps are {}", trumps);
        }
        return card;
    }

    private Set<Card> getAllowedCards(Trick trickSoFar) {
        if (trickSoFar.isEmpty()) {
            // TODO: breaking of trumps rule.
            return hand;
        }
        Suit leadingSuit = trickSoFar.leadingSuit();
        Set<Card> allowedCards = hand.stream()
                .filter(card -> card.suit() == leadingSuit)
                .collect(toSet());
        // Must follow suit if you can
        if (!allowedCards.isEmpty()) {
            return allowedCards;
        }
        logger.debug("I can't follow suit on {}", trickSoFar.leadingSuit());

        return hand;
    }

    public Set<Card> peekAtHand() {
        return ImmutableSet.copyOf(hand);
    }

    @Override
    public BiddingStrategy getStrategy() {
        return strategy;
    }

    @Override
    public boolean hasCards() {
        return !hand.isEmpty();
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
