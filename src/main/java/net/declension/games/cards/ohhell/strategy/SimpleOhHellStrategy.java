package net.declension.games.cards.ohhell.strategy;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.PlayerID;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleOhHellStrategy implements OhHellStrategy {

    private final Random rng;

    public SimpleOhHellStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards, SlotsMap<PlayerID, Integer> bidsSoFar, Set<Integer> allowedBids) {
        double expectedTaken = myCards.size() / bidsSoFar.getCapacity();
        Function<? super Integer, Double> distanceFromMean = bid -> (bid - expectedTaken) * (bid - expectedTaken);
        return allowedBids.stream().min(Comparator.comparing(distanceFromMean)).get();
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards, Map<PlayerID, BidAndTaken> bidsAndScores, Trick trickSoFar, Set<Card> allowedCards) {
        return pickRandomly(rng, allowedCards);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
