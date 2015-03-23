package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public interface TrumpsBasedBiddingStrategy extends BiddingStrategy {
    static final Logger LOGGER = LoggerFactory.getLogger(TrumpsBasedBiddingStrategy.class);
    @Override
    default Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                             AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        int handSize = myCards.size();
        trumps.orElseThrow(() -> new IllegalStateException("This strategy doesn't support no-trumps"));
        double numTrumps = trumps.isPresent()? myCards.stream().filter(card -> card.suit() == trumps.get()).count() : 0;
        double trumpsDelta = trumpsDeltaFor(handSize, numTrumps);
        LOGGER.debug("My hand: {}", myCards);
        return chooseLowestSquareUsingFunction(allowedBids,
                                               distanceFunction(trumpsDelta, handSize, bidsSoFar.capacity()));
    }

    static Function<Integer, Double> distanceFunction(double trumpsDelta, int handSize, int numPlayers) {
        double mean = (double) handSize / numPlayers;
        double weightedMean = (trumpsDelta + 1.0) * mean;
        LOGGER.debug("I reckon on getting {} tricks, not average {} ({} players)", weightedMean, mean, numPlayers);
        return bid -> weightedMean - bid;
    }

    static double trumpsDeltaFor(int handSize, double numTrumps) {
        double expectedTrumps = handSize * 12.0 / 51.0;
        double trumpsDelta = (numTrumps - expectedTrumps) / handSize ;
        LOGGER.debug("Trumps delta is {} (expected {}, got {})", trumpsDelta, expectedTrumps, numTrumps);
        return trumpsDelta;
    }

}
