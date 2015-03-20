package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public class SimpleStrategy implements Strategy {

    public static final String NAME = "Simple";
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleStrategy.class);
    private final Random rng;
    private final GameSetup gameSetup;

    public SimpleStrategy(Random rng, GameSetup gameSetup) {
        this.rng = rng;
        this.gameSetup = gameSetup;
    }

    @Override
    public Integer chooseBid(Suit trumps, Player me, Set<Card> myCards, AllBids bidsSoFar, Set<Integer> allowedBids) {
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }

    static Integer closestToMean(Set<Integer> allowedBids, AllBids bidsSoFar, int handSize) {
        double expectedTaken = (double) handSize / bidsSoFar.getCapacity();
        Function<Integer, Double> distanceFromMean = bid -> bid - expectedTaken;
        return chooseLowestSquareUsingFunction(allowedBids, distanceFromMean);
    }

    @Override
    public Card chooseCard(Suit trumps, Player me, Set<Card> myCards,
                           Map<Player, Integer> tricksBid, Map<Player, Integer> tricksTaken,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
        if (allowedCards.size() == 1) {
            return allowedCards.iterator().next();
        }

        int myScore = tricksTaken.get(me);
        int delta = myScore - tricksBid.get(me);
        final Comparator<Card> cmp = gameSetup.createTrickComparator(trumps, trickSoFar.leadingSuit());
        if (delta < 0) {
            LOGGER.debug("Aiming high (got {}, at {})!", myScore, delta);
            return highestAllowed(allowedCards, cmp);
        } else {
            LOGGER.debug("Playing to lose (got {}, at {})!", myScore, delta);
            if (!trickSoFar.isEmpty()) {

                // Play the highest card that could definitely lose;
                // We know it's there, as it's a non-empty trick as above.
                Card currentWinningCard = trickSoFar.currentWinningCard().get();
                LOGGER.debug("{} is currently winning", currentWinningCard);
                Comparator<Card> betterThan = (left, right) -> Integer.compare(cmp.compare(left, currentWinningCard),
                                                                               cmp.compare(right, currentWinningCard));
                Optional<Card> highestLosing = allowedCards.stream()
                        .sorted(betterThan.thenComparing(cmp))
                        .findFirst();
                if (highestLosing.isPresent()) {
                    LOGGER.debug("Gonna play the highest losing card: {}", highestLosing.get());
                    return highestLosing.get();
                }
            }
            return lowestAllowed(allowedCards, cmp);
        }
    }

    private Card lowestAllowed(Set<Card> allowedCards, Comparator<Card> cmp) {
        return allowedCards.stream().sorted(cmp).findFirst().get();
    }

    private Card highestAllowed(Set<Card> allowedCards, Comparator<Card> cmp) {
        return allowedCards.stream().sorted(cmp.reversed()).findFirst().get();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String toString() {
        return getName();
    }
}
