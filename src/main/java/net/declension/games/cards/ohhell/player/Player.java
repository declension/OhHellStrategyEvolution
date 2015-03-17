package net.declension.games.cards.ohhell.player;


import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.BiddingStrategy;

import java.util.Collection;

public interface Player  {

    PlayerID getID();

    void receiveNewHand(Suit trumps, Collection<Card> cards);

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Trick trickSoFar);

    BiddingStrategy getStrategy();

    boolean hasCards();
}