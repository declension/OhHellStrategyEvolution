package net.declension.games.cards.ohhell.player;

import com.google.common.collect.ImmutableSet;
import net.declension.games.cards.Card;
import net.declension.games.cards.CardSet;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.AllBids;
import net.declension.games.cards.ohhell.Game;
import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.Trick;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static net.declension.utils.Validation.requireNonNullParam;

public class BasicPlayer implements Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicPlayer.class);

    private final PlayerID playerID;
    private final GameSetup gameSetup;
    private OhHellStrategy strategy;
    private CardSet hand;
    private Optional<Suit> trumps;

    /**
     * Construct a new Player with an auto-generated PlayerID.
     * @param gameSetup the common setup of all games this player will play
     * @param strategy the strategy that this player uses for bidding and choosing cards to play.
     */
    public BasicPlayer(GameSetup gameSetup, OhHellStrategy strategy) {
        this(new PlayerID(), gameSetup, strategy);
    }

    public BasicPlayer(PlayerID playerID, GameSetup gameSetup, OhHellStrategy strategy) {
        requireNonNullParam(playerID, "Player ID");
        requireNonNullParam(strategy, "Game strategy");
        requireNonNullParam(gameSetup, "Game Setup");
        this.gameSetup = gameSetup;
        this.strategy = strategy;
        this.playerID = playerID;
    }

    @Override
    public void receiveNewHand(Optional<Suit> trumps, Collection<Card> cards) {
        this.trumps = trumps;
        hand = new CardSet(gameSetup.createDisplayComparator(trumps), cards);
    }

    @Override
    public Integer bid(Game game, AllBids bidsSoFar) {
        Set<Integer> allowedBids = game.getBidValidator().getAllowedBidsForPlayer(this, hand.size(), bidsSoFar);
        Integer bid = strategy.chooseBid(trumps, this, hand, bidsSoFar, allowedBids);
        LOGGER.debug("{} is bidding {} using {}", this, bid, strategy);
        return bid;
    }

    @Override
    public Card playCard(Game game, Trick trickSoFar) {
        Card card = chooseCard(game, trickSoFar);
        hand.remove(card);
        return card;
    }

    private Card chooseCard(Game game, Trick trickSoFar) {
        Set<Card> allowedCards = gameSetup.getRules().getAllowedCards(hand, game.getPlayedCards(), trickSoFar);
        // No point bothering the strategies if there's no choice involved...
        if (allowedCards.size() == 1) {
            return allowedCards.iterator().next();
        }
        Card card = strategy.chooseCard(game.getTrumps(), this, hand, game.getFinalTricksBid(), game.getTricksTaken(),
                                        trickSoFar, allowedCards);
        checkChosenCardWasAllowed(trickSoFar, allowedCards, card);
        return card;
    }

    private void checkChosenCardWasAllowed(Trick trickSoFar, Set<Card> allowedCards, Card card) {
        if (!allowedCards.contains(card)) {
            throw new IllegalArgumentException(
                    format("%s (using '%s' strategy) tried to play a %s from hand %s (trumps: %s, lead: %s)" +
                                    " when only allowed cards are %s in %s",
                            this, strategy, card, hand, trumps, trickSoFar.leadingSuit(), allowedCards, gameSetup));
        }
    }

    public Set<Card> peekAtHand() {
        return ImmutableSet.copyOf(hand);
    }

    @Override
    public OhHellStrategy getStrategy() {
        return strategy;
    }

    @Override
    public boolean hasCards() {
        return !hand.isEmpty();
    }

    @Override
    public PlayerID getID() {
        return playerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasicPlayer)) {
            return false;
        }
        BasicPlayer other = (BasicPlayer) o;
        return playerID.equals(other.playerID);

    }

    @Override
    public int hashCode() {
        return playerID.hashCode();
    }

    @Override
    public String toString() {
        return format("<%s-%s>", playerID, strategy);
    }

}
