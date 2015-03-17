package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Map;
import java.util.Set;

public interface OhHellStrategy {

    Integer chooseBid(Suit trumps,
                      Set<Card> myCards,
                      AllBids bidsSoFar,
                      Set<Integer> allowedBids);

    /**
     * All lists are in the same order. 0 is first player (to the left of the dealer).
     */
    Card chooseCard(Suit trumps,
                    Set<Card> myCards,
                    Map<? extends Player, BidAndTaken> bidsAndScores,
                    Trick trickSoFar,
                    Set<Card> allowedCards);

}
