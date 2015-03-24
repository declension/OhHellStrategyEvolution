package net.declension.games.cards.ohhell.player;

import net.declension.games.cards.Card;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.Collection;
import java.util.Optional;

public class DummyPlayer implements Player<OhHellStrategy> {
    private final PlayerID playerID;

    public DummyPlayer() {
        this(new PlayerID());
    }

    public DummyPlayer(String id) {
        this(new PlayerID(id));
    }

    public DummyPlayer(PlayerID playerID) {
        this.playerID = playerID;
    }

    @Override
    public PlayerID getID() {
        return playerID;
    }

    @Override
    public void receiveNewHand(Optional<Suit> trumps, Collection<Card> cards) {

    }

    @Override
    public Integer bid(Game game, AllBids bidsSoFar) {
        return null;
    }

    @Override
    public Card playCard(Game game, Trick trickSoFar) {
        return null;
    }

    @Override
    public OhHellStrategy getStrategy() {
        return null;
    }

    @Override
    public boolean hasCards() {
        return false;
    }
}
