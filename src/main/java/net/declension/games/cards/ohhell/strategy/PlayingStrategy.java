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
     * @param trickSoFar The state of the current trick. {@code null} means not yet played.
     */
    Card chooseCard(Suit trumps,
                    Player me,
                    Set<Card> myCards,
                    Map<Player, Integer> tricksBid,
                    Map<Player, Integer> tricksTaken,
                    Trick trickSoFar,
                    Set<Card> allowedCards);
}
