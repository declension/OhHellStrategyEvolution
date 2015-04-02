package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;

import java.util.Deque;
import java.util.Set;

public interface OhHellRules {
    Set<Card> getAllowedCards(Set<Card> hand, Deque<Card> played, Trick trickSoFar);

    /**
     * The maximum number of players for a game of a given size
     *
     * @param numPlayers The number of players in the game
     * @return an integer between 1 (!) and 52, (but more likely 17)
     */
    int maximumCardsFor(int numPlayers);
}
