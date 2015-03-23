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
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static net.declension.utils.OptionalUtils.optionalToString;
import static net.declension.utils.Validation.requireNonNullParam;

public class BasicPlayer implements Player {
    private final Logger logger;

    private final PlayerID playerID;
    private final GameSetup gameSetup;
    private Strategy strategy;
    private CardSet hand;
    private Optional<Suit> trumps;

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
    public void receiveNewHand(Optional<Suit> trumps, Collection<Card> cards) {
        this.trumps = trumps;
        hand = new CardSet(gameSetup.createRoundComparator(trumps), cards);
    }

    @Override
    public Integer bid(Game game, AllBids bidsSoFar) {
        Set<Integer> allowedBids = game.getBidValidator().getAllowedBidsForPlayer(this, hand.size(), bidsSoFar);
        //logger.debug("Bids allowed: {}", allowedBids);
        Integer bid = strategy.chooseBid(trumps, this, hand, bidsSoFar, allowedBids);
        logger.debug("{} is bidding {} using {}", this, bid, strategy);
        return bid;
    }

    @Override
    public synchronized Card playCard(Game game, Trick trickSoFar) {
        Card card = chooseCard(game, trickSoFar);
        if (card.rank() == Rank.ACE && trumps.isPresent() && card.suit() == trumps.get()) {
            logger.info("Hand 'em over people, trumps are {}", optionalToString(trumps));
        }
        hand.remove(card);
        return card;
    }

    private Card chooseCard(Game game, Trick trickSoFar) {
        Set<Card> allowedCards = getAllowedCards(trickSoFar);
        // No point bothering the strategies if there's no choice involved...
        if (allowedCards.size() == 1) {
            return allowedCards.iterator().next();
        }
        Card card = strategy.chooseCard(game.getTrumps(), this, hand, game.getFinalTricksBid(), game.getTricksTaken(),
                                   trickSoFar, allowedCards);
        checkChosenCardWasAllowed(trickSoFar, allowedCards, card);
        return card;
    }

    private void checkChosenCardWasAllowed(Trick trickSoFar, Set<Card> allowedCards, Card card) {
        if (!allowedCards.contains(card)) {
            throw new IllegalArgumentException(
                    format("%s (using '%s' strategy) tried to play a %s from hand %s (trumps: %s, lead: %s)" +
                                    " when only allowed cards are %s in %s",
                            this, strategy, card, hand, trumps, trickSoFar.leadingSuit(), allowedCards, gameSetup));
        }
    }

    private Set<Card> getAllowedCards(Trick trickSoFar) {
        if (trickSoFar.isEmpty()) {
            // TODO: breaking of trumps rule.
            return hand;
        }
        Suit leadingSuit = trickSoFar.leadingSuit().get();
        Set<Card> allowedCards = hand.stream()
                .filter(card -> card.suit() == leadingSuit)
                .collect(toSet());
        // Must follow suit if you can
        if (!allowedCards.isEmpty()) {
            return allowedCards;
        }
        logger.debug("I can't follow suit on {}", leadingSuit);

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

    @Override
    public String toString() {
        return playerID.toString();
    }

}
