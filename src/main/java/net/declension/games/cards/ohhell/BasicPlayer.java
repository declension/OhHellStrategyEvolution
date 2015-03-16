package net.declension.games.cards.ohhell;

import com.google.common.collect.ImmutableSet;
import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Suit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;
import static net.declension.Utils.requireNonNullParam;

public class BasicPlayer implements Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);
    private final UUID id;

    private OhHellStrategy strategy;
    private CardSet hand;
    private final GameSetup gameSetup;
    private Suit trumps;


    public BasicPlayer(OhHellStrategy strategy, GameSetup gameSetup) {
        this(UUID.randomUUID(), strategy, gameSetup);
    }

    public BasicPlayer(UUID id, OhHellStrategy strategy, GameSetup gameSetup) {
        requireNonNullParam(gameSetup, "Game setup");
        requireNonNullParam(id, "ID");
        requireNonNullParam(strategy, "Game strategy");
        this.gameSetup = gameSetup;
        this.strategy = strategy;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Player #%d", Math.abs(id.hashCode()) % 10000);
    }


    @Override
    public void receiveNewHand(Suit trumps, Collection<Card> cards) {
        this.trumps = trumps;
        hand = new CardSet(trumps);
        hand.addAll(cards);
    }

    @Override
    public Short bid(SlotsMap<Player, Short> bidsSoFar) {
        int handSize = hand.size();
        Set<Short> allowedBids = IntStream.rangeClosed(1, handSize).mapToObj(i -> (short) i).collect(toSet());
        Short bid = strategy.chooseBid(gameSetup, trumps, hand, bidsSoFar, allowedBids);
        LOGGER.debug("{} is bidding {} using {}", this, bid, strategy);
        return bid;
    }


    @Override
    public Card playCard(Map<Player, Short> bids, SlotsMap<Player, Card> trickSoFar) {
        return null;
    }

    @Override
    public Set<Card> hand() {
        return ImmutableSet.copyOf(hand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicPlayer)) return false;

        BasicPlayer player = (BasicPlayer) o;

        return id.equals(player.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
