package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;

import java.util.Set;

/**
 * How a player should bid before starting a round.
 */
public interface BiddingStrategy {
    Integer chooseBid(Suit trumps,
                      Set<Card> myCards,
                      AllBids bidsSoFar,
                      Set<Integer> allowedBids);
}
