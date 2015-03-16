package net.declension.games.cards.ohhell;

import com.google.common.collect.Lists;
import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.AceHighRankComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    public static final AceHighRankComparator RANK_COMPARATOR = new AceHighRankComparator();

    private final List<Player> players;
    GameSetup setup;

    private Scorer scorer;
    private Predicate<SlotsMap<Player, Short>> bidValidator;
    private Suit currentTrumps;

    public Game(List<Player> players) {
        this.players = players;
        LOGGER.info("Setting up {} players for this game: {}", players.size(), players);
        //cardsForPlayers = new SlotsMap<>(players, () -> new CardSet(getCurrentTrumps()));
    }

    public void playRound(Short handSize) {
        Deck deck = new Deck().shuffled();
        LOGGER.debug(deck.toString());

        deal(handSize, deck, players);

        SlotsMap<Player, Short> bids = new SlotsMap<>(players, () -> (short) 0);
        players.stream().forEach(player -> {
            Short bid = player.bid(bids);
            // Bid is "guaranteed" valid
            bids.put(player, bid);
        });
        LOGGER.info("Here are the bids: {}", bids);
    }

    private void deal(Short number, Deck deck, List<Player> players) {
        List<Card> dealtCards = deck.pullCards(players.size() * number);

        // It's fully random, so don't have to deal one card at a time - just give n cards to each player
        giveHandsToPlayers(players, Lists.partition(dealtCards, number));

        LOGGER.debug("After dealing: {}", deck);
        LOGGER.info("{}", players);
    }

    /**
     * A would-be ZIP method.
     * @param players the players
     * @param hands the list of hands to be given out, <strong>in the same order</strong>.
     */
    private void giveHandsToPlayers(List<Player> players, List<List<Card>> hands) {
        Iterator<Player> itr = players.iterator();
        for (int i =0; i < players.size(); i++) {
            itr.next().receiveNewHand(currentTrumps, hands.get(i));
        }
    }

    public Suit getCurrentTrumps() {
        return currentTrumps;
    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", setup=" + setup +
                ", scorer=" + scorer +
                ", bidValidator=" + bidValidator +
                ", currentTrumps=" + currentTrumps +
                '}';
    }
}
