package net.declension.games.cards.ohhell;

import net.declension.games.cards.Card;

public interface FirstCardListener<T> {

    void onFirstCard(T trick, Card firstCard);
}
