package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public class TrumpsBasedRandomStrategy extends AverageStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrumpsBasedRandomStrategy.class);

    public TrumpsBasedRandomStrategy(GameSetup gameSetup) {
        super(gameSetup.getRNG());
    }

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                             AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        int handSize = myCards.size();
        trumps.orElseThrow(() -> new IllegalStateException("This strategy doesn't support no-trumps"));
        double numTrumps = trumps.isPresent()? myCards.stream().filter(card -> card.suit() == trumps.get()).count() : 0;
        double trumpsDelta = trumpsDeltaFor(handSize, numTrumps);
        LOGGER.debug("My hand: {}", myCards);
        return chooseLowestSquareUsingFunction(allowedBids, distanceFunction(trumpsDelta, handSize));
    }

    private Function<Integer, Double> distanceFunction(double trumpsDelta, double handSize) {
        // e.g. handSize of 6 => 0...6. Midpoint is 0,1,2,[3],4,5,6
        // => / midpoint, subtract 1 => -1...+1
        // Then just get the closest trumpsDelta to that
        double midPoint = handSize / 2.0;
        return bid -> trumpsDelta - (bid / midPoint - 1.0);
    }

    static double trumpsDeltaFor(int handSize, double numTrumps) {
        double expectedTrumps = handSize * 12.0 / 51.0;
        double trumpsDelta = (numTrumps - expectedTrumps) / handSize;
        LOGGER.debug("Trumps delta is {} (expected {}, got {})", trumpsDelta, expectedTrumps, numTrumps);
        return trumpsDelta;
    }

    @Override
    public String getName() {
        return "Trumps";
    }
}
