package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Optional.empty;

public class Trick extends SlotsMap<Player, Optional<Card>> {
    private Optional<Comparator<Card>> cardOrdering = empty();
    private final Collection<FirstCardListener> firstCardListeners = new HashSet<>();
    private Optional<Card> firstCard = empty();

    public Trick(Collection<? extends Player> allKeys, FirstCardListener<Trick> listener) {
        this(allKeys);
        firstCardListeners.add(listener);
    }

    public Trick(Collection<? extends Player> allKeys) {
        super(allKeys, empty());
    }

    public Optional<Suit> leadingSuit() {
        return firstCard.map(Card::suit);
    }

    /**
     * This is needed as the ordering is not set until the first card is played.
     * @param cardOrdering the ordering to use on this trick
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
        return previous == null? empty() : previous;
    }

    public Optional<Player> winningPlayer() {
        if (!cardOrdering.isPresent()) {
            return empty();
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
            return empty();
        }
        return values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(cardOrdering.get());
    }
}
