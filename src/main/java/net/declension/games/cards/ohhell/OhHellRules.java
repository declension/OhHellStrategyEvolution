package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;

import java.util.Deque;
import java.util.Set;

public interface OhHellRules {
    Set<Card> getAllowedCards(Set<Card> hand, Deque<Card> played, Trick trickSoFar);
}
