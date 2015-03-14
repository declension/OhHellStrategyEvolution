package net.declension.games.cards.ohhell;

import net.declension.ea.cards.ohhell.GameSetup;
import net.declension.ea.cards.ohhell.Player;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.utils.SlotsMap;

import java.util.Set;

public interface OhHellStrategy {

    Short chooseBid(GameSetup gameSetup,
                    Suit trumps,
                    Set<Card> myCards,
                    SlotsMap<Player, Short> bidsSoFar,
                    Set<Short> allowedBids);

    /**
     * All lists are in the same order. 0 is first player (to the left of the dealer).
     */
    Card chooseCard(GameSetup gameSetup,
                    Suit trumps,
                    Set<Card> myCards,
                    SlotsMap<Player, Short> bids,
                    SlotsMap<Player, Card> handSoFar,
                    Set<Card> allowedCards);
}
