package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Comparator.comparing;

public class Trick extends SlotsMap<Player, Optional<Card>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trick.class);
    private final Collection<? extends Player> allKeys;
    private final Optional<Suit> trumps;
    private Optional<Comparator<Card>> cardOrdering = Optional.empty();
    private final Collection<FirstCardListener> firstCardListeners = new HashSet<>();
    private Optional<Card> firstCard = Optional.empty();

    public Trick(Collection<? extends Player> allKeys, Optional<Suit> trumps, FirstCardListener<Trick> listener) {
        this(allKeys, trumps);
        firstCardListeners.add(listener);
    }

    public Trick(Collection<? extends Player> allKeys, Optional<Suit> trumps) {
        super(allKeys, Optional::empty);
        this.allKeys = allKeys;
        this.trumps = trumps;
    }

    public Trick(Collection<? extends Player> allKeys, Optional<Suit> trumps, Comparator<Card> cardOrdering) {
        this(allKeys, trumps);
        setCardOrdering(cardOrdering);
    }

    public Optional<Suit> leadingSuit() {
        return firstCard.map(Card::suit);
    }

    /**
     * This is needed as the ordering is not set until the first card is played.
     * @param cardOrdering
     */
    public void setCardOrdering(Comparator<Card> cardOrdering) {
        this.cardOrdering = Optional.of(cardOrdering);
    }

    @Override
    public void putAll(Map<? extends Player, ? extends Optional<Card>> incoming) {
        throw new UnsupportedOperationException("Trick must be put one by one");
    }

    /**
     * Traditional (non-{@link Optional}-based) writing to the data.
     *
     * @param player the player
     * @param card the card for that player.
     * @return The previous value associated with this entry.
     */
    public Optional<Card> put(Player player, Card card) {
        return put(player, Optional.of(card));
    }


    @Override
    public Optional<Card> put(Player player, Optional<Card> card) {
        if (size() == 0) {
            firstCard = card;
            firstCardListeners.forEach(listener -> listener.onFirstCard(this, card.get()));
        }
        Optional<Card> previous = super.put(player, card);
        return previous == null? Optional.empty() : previous;
    }

    public Optional<Player> winningPlayer() {
        if (!cardOrdering.isPresent()) {
            return Optional.empty();
        }
        Comparator<Entry<Player, Optional<Card>>> entryComparator
                = comparing(e -> e.getValue().orElse(null), cardOrdering.get());
        return entrySet().stream()
                .filter(e -> e.getValue().isPresent())
                .max(entryComparator)
                .map(Entry::getKey);
    }

    public Optional<Card> currentWinningCard() {
        if (!cardOrdering.isPresent()) {
            return Optional.empty();
        }
        return values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(cardOrdering.get());
    }
}
