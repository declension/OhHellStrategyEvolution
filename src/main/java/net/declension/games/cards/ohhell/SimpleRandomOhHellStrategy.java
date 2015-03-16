package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleRandomOhHellStrategy implements OhHellStrategy {

    private Random rng;

    public SimpleRandomOhHellStrategy(Random rng) {
        this.rng = rng;
    }

    @Override
    public Integer chooseBid(Suit trumps, Set<Card> myCards,
                             SlotsMap<PlayerID, Integer> bidsSoFar,
                             Set<Integer> allowedBids) {
        return pickRandomly(rng, allowedBids);
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards,
                           Map<PlayerID, BidAndTaken> bidsAndScores,
                           SlotsMap<PlayerID, Card> trickSoFar,
                           Set<Card> allowedCards) {
        return pickRandomly(rng, allowedCards);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
