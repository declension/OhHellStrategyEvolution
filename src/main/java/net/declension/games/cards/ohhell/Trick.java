package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;

public class Trick extends SlotsMap<Player, Card> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trick.class);
    private Comparator<Card> cardOrdering;

    private final Collection<FirstCardListener> firstCardListeners;
    private Card firstCard;

    public Trick(Collection<? extends Player> allKeys) {
        super(allKeys);
        firstCardListeners = new HashSet<>();
    }

    public Trick(Collection<? extends Player> allKeys, FirstCardListener<Trick>... listeners) {
        super(allKeys);
        firstCardListeners = asList(listeners);
    }

    public Trick(Collection<? extends Player> allKeys, Comparator<Card> cardOrdering) {
        this(allKeys);
        this.cardOrdering = cardOrdering;
    }

    public Suit leadingSuit() {
        return firstCard == null? null : firstCard.suit();
    }

    public void setCardOrdering(Comparator<Card> cardOrdering) {
        this.cardOrdering = cardOrdering;
    }

    @Override
    public void putAll(Map<? extends Player, ? extends Card> incoming) {
        throw new UnsupportedOperationException("Trick must be put one by one");
    }

    @Override
    public Card put(Player player, Card card) {
        if (size() == 0) {
            firstCard = card;
            firstCardListeners.forEach(listener -> listener.onFirstCard(this, card));
        }
        return super.put(player, card);
    }

    public Player winningPlayer() {
        if (cardOrdering == null) {
            throw new IllegalStateException("No ordering set - can't find winner");
        }
        Comparator<Entry<Player, Card>> entryComparator = comparing(Entry::getValue, cardOrdering);
        return entrySet().stream().max(entryComparator).get().getKey();
    }
}
