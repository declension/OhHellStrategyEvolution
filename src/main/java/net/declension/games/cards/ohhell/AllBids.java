package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;
import net.declension.games.cards.ohhell.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents all the bids for a round, made or yet to be made (which will have a {@code null} value)
 */
public class AllBids extends SlotsMap<Player, Integer> {

    public AllBids(Collection<Player> allKeys) {
        super(allKeys);
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
        updated.put(player, bid);
        return updated;
    }

}
