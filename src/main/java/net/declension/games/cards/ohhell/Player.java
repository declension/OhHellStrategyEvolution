package net.declension.games.cards.ohhell;


import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.Collection;
import java.util.Set;

public interface Player  {

    PlayerID getID();

    void receiveNewHand(Suit trumps, Collection<Card> cards);

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Trick trickSoFar);

    Set<Card> hand();

    OhHellStrategy getStrategy();
}
