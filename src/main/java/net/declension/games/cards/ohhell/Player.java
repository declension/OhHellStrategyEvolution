package net.declension.games.cards.ohhell;


import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.tricks.BidAndTaken;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Player  {

    PlayerID getID();

    void receiveNewHand(Suit trumps, Collection<Card> cards);

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Map<PlayerID, BidAndTaken> bidAndTakens, SlotsMap<PlayerID, Card> trickSoFar);

    Set<Card> hand();
}
