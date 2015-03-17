package net.declension.games.cards.ohhell.strategy;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;
import java.util.Set;

/**
 * How the player should play given a set of cards and some stats.
 */
public interface PlayingStrategy {
    /**
     * All lists are in the same order. 0 is first player (to the left of the dealer).
     */
    Card chooseCard(Suit trumps,
                    Set<Card> myCards,
                    Map<? extends Player, Integer> tricksBid,
                    Map<? extends Player, Integer> tricksTaken,
                    Trick trickSoFar,
                    Set<Card> allowedCards);
}
