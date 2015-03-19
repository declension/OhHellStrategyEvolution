package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.chooseLowestSquareUsingFunction;
import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleStrategy implements Strategy {

    public static final String NAME = "Simple";
    private transient final Random rng;

    public SimpleStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards, AllBids bidsSoFar, Set<Integer> allowedBids) {
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }

    static Integer closestToMean(Set<Integer> allowedBids, AllBids bidsSoFar, int handSize) {
        double expectedTaken = (double) handSize / bidsSoFar.getCapacity();
        Function<Integer, Double> distanceFromMean = bid -> bid - expectedTaken;
        return chooseLowestSquareUsingFunction(allowedBids, distanceFromMean);
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards,
                           Map<? extends Player, Integer> tricksBid, Map<? extends Player, Integer> tricksTaken,
                           Trick trickSoFar, Set<Card> allowedCards) {
        return pickRandomly(rng, allowedCards);
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
