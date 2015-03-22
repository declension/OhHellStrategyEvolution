package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;

public class AverageStrategy extends RandomStrategy {

    public static final String NAME = "Average";

    public AverageStrategy(Random rng) {
        super(rng);
    }

    @Override
    public Integer chooseBid(Optional<Suit> trumps, Player me, Set<Card> myCards,
                             AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }

    static Integer closestToMean(Set<Integer> allowedBids, AllBids bidsSoFar, int handSize) {
        double expectedTaken = (double) handSize / bidsSoFar.getCapacity();
        Function<Integer, Double> distanceFromMean = bid -> bid - expectedTaken;
        return chooseLowestSquareUsingFunction(allowedBids, distanceFromMean);
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
