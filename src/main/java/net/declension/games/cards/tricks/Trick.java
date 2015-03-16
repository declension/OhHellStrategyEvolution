package net.declension.games.cards.tricks;

import com.google.common.collect.ImmutableSet;
import net.declension.games.cards.Card;
import net.declension.games.cards.ohhell.BasicPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Trick {
    private Map<BasicPlayer, Card> cardMap = new HashMap<>();
    private BasicPlayer winner;
    private final ImmutableSet<BasicPlayer> players;

    public Trick(Collection<BasicPlayer> players) {
        this.players = ImmutableSet.copyOf(players);
    }

    public void placeCard(BasicPlayer player, Card card) {
        cardMap.put(player, card);
        if (cardMap.keySet() == players) {

        }
    }


}
