package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SimpleOhHellStrategy implements OhHellStrategy {

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards, SlotsMap<PlayerID, Integer> bidsSoFar, Set<Integer> allowedBids) {
        double expectedTaken = myCards.size() / bidsSoFar.getCapacity();
        Function<? super Integer, Double> distanceFromMean = bid -> (bid - expectedTaken) * (bid - expectedTaken);
        return allowedBids.stream().min(Comparator.comparing(distanceFromMean)).get();
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards, Map<PlayerID, BidAndTaken> bidsAndScores, SlotsMap<PlayerID, Card> trickSoFar, Set<Card> allowedCards) {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
