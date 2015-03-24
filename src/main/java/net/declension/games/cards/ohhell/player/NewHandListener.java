package net.declension.games.cards.ohhell.player;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;

import java.util.Collection;
import java.util.Optional;

public interface NewHandListener {

    void receiveNewHand(Optional<Suit> trumps, Collection<Card> cards);
}
