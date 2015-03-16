package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Trick extends SlotsMap<PlayerID, Card> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trick.class);
    private Comparator<Card> cardOrdering;

    private final Collection<FirstCardListener> firstCardListeners = new HashSet<>();
    public Trick(Collection<PlayerID> allKeys) {
        super(allKeys);
    }

    public Trick(Collection<PlayerID> allKeys, Comparator<Card> cardOrdering) {
        super(allKeys);
        this.cardOrdering = cardOrdering;
    }

    public void setCardOrdering(Comparator<Card> cardOrdering) {
        this.cardOrdering = cardOrdering;
    }

    public static Trick forPlayers(Collection<Player> players) {
        return new Trick(players.stream().map(Player::getID).collect(toList()));
    }

    @Override
    public void putAll(Map<? extends PlayerID, ? extends Card> incoming) {
        throw new UnsupportedOperationException("Trick must be put one by one");
    }

    @Override
    public Card put(PlayerID key, Card card) {
        if (size() == 0) {
            LOGGER.debug("Firing first card listener for {}", card);
            firstCardListeners.forEach(listener -> listener.onFirstCard(this, card));
        }
        return super.put(key, card);
    }

    public void addFirstCardListener(FirstCardListener listener) {
        firstCardListeners.add(listener);
    }

    public PlayerID winningPlayer() {
        if (cardOrdering == null) {
            throw new IllegalStateException("No ordering set - can't find winner");
        }
        Comparator<Entry<PlayerID, Card>> comparing = comparing(Entry::getValue, cardOrdering);
        return entrySet().stream().max(comparing).get().getKey();
    }
}
