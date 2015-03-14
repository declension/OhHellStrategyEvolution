package net.declension.ea.cards.ohhell;

import com.google.common.collect.ImmutableSet;
import net.declension.games.cards.Card;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Trick {
    private Map<Player, Card> cardMap = new HashMap<>();
    private Player winner;
    private final ImmutableSet<Player> players;

    public Trick(Collection<Player> players) {
        this.players = ImmutableSet.copyOf(players);
    }

    public void placeCard(Player player, Card card) {
        cardMap.put(player, card);
        if (cardMap.keySet() == players) {

        }
    }


}
