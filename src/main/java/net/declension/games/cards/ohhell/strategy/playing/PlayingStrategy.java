package net.declension.games.cards.ohhell.strategy.playing;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * How the player should play given a set of cards and some stats.
 */
public interface PlayingStrategy {
    /**
     * @param trumps the trumps for this trick, if any.
     * @param trickSoFar The state of the current trick.
     */
    Card chooseCard(Optional<Suit> trumps,
                    Player me,
                    Set<Card> myCards,
                    Map<Player, Integer> tricksBid,
                    Map<Player, Integer> tricksTaken,
                    Trick trickSoFar,
                    Set<Card> allowedCards);
}
