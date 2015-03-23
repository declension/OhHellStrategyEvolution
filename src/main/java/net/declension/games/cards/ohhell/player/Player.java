package net.declension.games.cards.ohhell.player;


import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.bidding.BiddingStrategy;

import java.util.Collection;
import java.util.Optional;

public interface Player  {

    PlayerID getID();

    void receiveNewHand(Optional<Suit> trumps, Collection<Card> cards);

    Integer bid(Game game, AllBids bidsSoFar);

    Card playCard(Game game, Trick trickSoFar);

    BiddingStrategy getStrategy();

    boolean hasCards();
}
