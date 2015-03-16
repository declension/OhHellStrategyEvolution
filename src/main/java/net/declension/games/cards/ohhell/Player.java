package net.declension.games.cards.ohhell;


import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Player  {

    void receiveNewHand(Suit trumps, Collection<Card> cards);

    Short bid(SlotsMap<Player, Short> bidsSoFar);

    Card playCard(Map<Player, Short> bids, SlotsMap<Player, Card> trickSoFar);

    Set<Card> hand();
}
