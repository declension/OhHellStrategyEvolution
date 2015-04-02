package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class StandardRules implements OhHellRules {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandardRules.class);

    @Override
    public Set<Card> getAllowedCards(Set<Card> hand, Deque<Card> played, Trick trickSoFar) {
        if (trickSoFar.isEmpty()) {
            // TODO: breaking of trumps rule.
            return hand;
        }
        Suit leadingSuit = trickSoFar.leadingSuit().get();
        Set<Card> allowedCards = hand.stream()
                                     .filter(card -> card.suit() == leadingSuit)
                                     .collect(toSet());
        // Must follow suit if you can
        if (!allowedCards.isEmpty()) {
            return allowedCards;
        }
        LOGGER.debug("Can't follow suit on {}", leadingSuit);

        return hand;
    }

    @Override
    public int maximumCardsFor(int numPlayers) {
        return 51 / numPlayers;
    }
}
