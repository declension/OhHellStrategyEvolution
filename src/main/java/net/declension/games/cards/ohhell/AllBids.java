package net.declension.games.cards.ohhell;

import net.declension.collections.SlotsMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents all the bids for a round, made or yet to be made (which will have a {@code null} value)
 */
public class AllBids extends SlotsMap<PlayerID, Integer> {

    public AllBids(Collection<PlayerID> allKeys) {
        super(allKeys);
    }

    /**
     * Returns a copy of this map with a single update.
     *
     * @param playerID the new player
     * @param bid the bid for that new playerID
     * @return a copy of these Bids with the additional update
     */
    public AllBids copyWithBid(PlayerID playerID, Integer bid) {
        Set<PlayerID> keys = new HashSet<>(keySet());
        keys.add(playerID);
        AllBids updated = new AllBids(keys);
        updated.putAll(this);
        updated.put(playerID, bid);
        return updated;
    }

}
