package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleRandomStrategy implements Strategy {
    private static final String NAME = "Random";
    private transient final Random rng;

    public SimpleRandomStrategy(Random rng) {
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
                           Map<? extends Player, Integer> tricksBids,
                           Map<? extends Player, Integer> tricksTaken,
                           Trick trickSoFar,
                           Set<Card> allowedCards) {
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
