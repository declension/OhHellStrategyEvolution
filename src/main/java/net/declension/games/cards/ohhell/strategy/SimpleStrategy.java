package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleStrategy implements Strategy {

    private static final Function<Double, Double> SQUARE = x -> x * x;
    private transient final Random rng;

    public SimpleStrategy(Random rng, GameSetup gameSetup) {
        this.rng = rng;
    }

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards, AllBids bidsSoFar, Set<Integer> allowedBids) {
        return closestToMean(allowedBids, bidsSoFar, myCards.size());
    }

    static Integer closestToMean(Set<Integer> allowedBids, AllBids bidsSoFar, int handSize) {
        double expectedTaken = (double) handSize / bidsSoFar.getCapacity();
        Function<Integer, Double> distanceFromMean = bid -> bid - expectedTaken;
        return closestToValue(allowedBids, distanceFromMean);
    }

    private static <T> T closestToValue(Collection<T> values, Function<T, Double> distanceFunction) {
        return values.stream().min(comparing(SQUARE.compose(distanceFunction))).get();
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards,
                           Map<? extends Player, Integer> tricksBid, Map<? extends Player, Integer> tricksTaken,
                           Trick trickSoFar, Set<Card> allowedCards) {
        return pickRandomly(rng, allowedCards);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
