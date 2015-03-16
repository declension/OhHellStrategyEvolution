package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;

import java.util.Map;
import java.util.Set;

import static net.declension.collections.CollectionUtils.pickRandomly;

public class SimpleRandomOhHellStrategy implements OhHellStrategy {

    @Override
    public Short chooseBid(GameSetup gameSetup, Suit trumps, Set<Card> myCards,
                           SlotsMap<Player, Short> bidsSoFar, Set<Short> allowedBids) {
        return pickRandomly(gameSetup.getRNG(), allowedBids);
    }

    @Override
    public Card chooseCard(GameSetup gameSetup, Suit trumps, Set<Card> myCards, Map<Player, Short> bids,
                           SlotsMap<Player, Card> handSoFar, Set<Card> allowedCards) {
        return pickRandomly(gameSetup.getRNG(), allowedCards);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
