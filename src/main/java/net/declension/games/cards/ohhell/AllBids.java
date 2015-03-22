package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.ohhell.player.Player;

import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * Represents all the bids for a round, made or yet to be made (which will have a {@code null} value)
 */
public class AllBids extends SlotsMap<Player, Optional<Integer>> {

    public AllBids(Collection<? extends Player> allKeys) {
        super(allKeys, Optional::empty);
    }

    /**
     * Returns a copy of this map with a single update.
     *
     * @param player the new player
     * @param bid the bid for that new player
     * @return a copy of these Bids with the additional update
     */
    public AllBids copyWithBid(Player player, Integer bid) {
        Set<Player> keys = new HashSet<>(keySet());
        keys.add(player);
        AllBids updated = new AllBids(keys);
        updated.putAll(this);
        updated.put(player, Optional.of(bid));
        return updated;
    }

    /**
     * Semi-traditional usage to set a player's score. Doesn't return anything.
     *
     * @param player the player whose score to modify
     * @param bid the bid to set it to
     */
    public void put(Player player, Integer bid) {
       super.put(player, Optional.of(bid));
    }

    /**
     * Gets the final bids for each player.
     *
     * @throws IllegalStateException if the bids aren't in fact finished.
     * @return a map of finalised bids over Players.
     */
    public Map<Player, Integer> getFinalBids() {
        if (!isDone()) {
            throw new IllegalStateException("Not finished bidding: " + this);
        }
        return entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().get()))
                .collect(toMap(Entry::getKey, Entry::getValue, (l,r) -> l, () -> new SlotsMap<>(keySet())));
    }
}
