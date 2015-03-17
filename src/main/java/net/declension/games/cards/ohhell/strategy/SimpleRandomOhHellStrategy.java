package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.Trick;
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
                             AllBids bidsSoFar,
                             Set<Integer> allowedBids) {
        return pickRandomly(rng, allowedBids);
    }

    @Override
    public Card chooseCard(Suit trumps, Set<Card> myCards,
                           Map<Player, BidAndTaken> bidsAndScores,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
        return pickRandomly(rng, allowedCards);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
