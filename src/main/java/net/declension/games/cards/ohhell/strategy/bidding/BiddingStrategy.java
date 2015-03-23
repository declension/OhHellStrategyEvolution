package net.declension.games.cards.ohhell.strategy.bidding;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Optional;
import java.util.Set;

/**
 * How a player should bid before starting a round.
 */
public interface BiddingStrategy {
    Integer chooseBid(Optional<Suit> trumps,
                      Player me,
                      Set<Card> myCards,
                      AllBids bidsSoFar,
                      Set<Integer> allowedBids);
}
