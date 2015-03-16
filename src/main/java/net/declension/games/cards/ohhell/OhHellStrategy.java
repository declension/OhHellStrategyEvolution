package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Map;
import java.util.Set;

public interface OhHellStrategy {

    Integer chooseBid(Suit trumps,
                      Set<Card> myCards,
                      SlotsMap<PlayerID, Integer> bidsSoFar,
                      Set<Integer> allowedBids);

    /**
     * All lists are in the same order. 0 is first player (to the left of the dealer).
     */
    Card chooseCard(Suit trumps,
                    Set<Card> myCards,
                    Map<PlayerID, BidAndTaken> bidsAndScores,
                    SlotsMap<PlayerID, Card> trickSoFar,
                    Set<Card> allowedCards);

}
