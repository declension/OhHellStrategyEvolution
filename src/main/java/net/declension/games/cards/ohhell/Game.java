package net.declension.games.cards.ohhell;

import com.google.common.collect.Lists;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.sorting.AceHighRankComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);
    public static final AceHighRankComparator RANK_COMPARATOR = new AceHighRankComparator();

    private final List<Player> players;
    private final GameSetup setup;

    private Scorer scorer;
    private Suit currentTrumps;
    private BidValidator bidValidator;

    public Game(List<Player> players, GameSetup setup) {
        this.players = players;
        this.setup = setup;
        LOGGER.info("Setting up {} players for this game: {}", players.size(), players);
        //cardsForPlayers = new SlotsMap<>(players, () -> new CardSet(getCurrentTrumps()));
    }

    public void playRound(Integer handSize) {
        Deck deck = new Deck().shuffled();
        deal(handSize, deck, players);

        bidValidator = new BidBustingRulesBidValidator(handSize);
        AllBids bids = new AllBids(getPlayerIDs());

        takeBids(handSize, bids);
    }

    private void takeBids(Integer handSize, AllBids bids) {
        players.stream().forEach(player -> {
            Integer bid = player.bid(this, bids);
            bids.put(player.getID(), bid);
            // Bid is "guaranteed" valid
            if (!bidValidator.test(bids)) {
                throw new IllegalStateException(
                        format("Oh dear: %s had tried a dodgy bid of %d for a trick of size %d. The rest: %s",
                                player, bid, handSize, bids));
            }
        });
        LOGGER.info("Here are the bids: {}. Total={}", bids, bids.values().stream().mapToInt(v -> (int) v).sum());
    }

    private List<PlayerID> getPlayerIDs() {
        return players.stream().map(Player::getID).collect(toList());
    }

    private void deal(Integer number, Deck deck, List<Player> players) {
        LOGGER.info("Dealing {} cards each to the {} players...", number, players.size());
        List<Card> dealtCards = deck.pullCards(players.size() * number);

        // It's fully random, so don't have to deal one card at a time - just give n cards to each player
        giveHandsToPlayers(players, Lists.partition(dealtCards, number));

        LOGGER.debug("After dealing: {}", deck);
        LOGGER.info("{}", players);
    }

    /**
     * A would-be {@code zip} method.
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
                ", currentTrumps=" + currentTrumps +
                '}';
    }

    public BidValidator getBidValidator() {
        return bidValidator;
    }

    public GameSetup getSetup() {
        return setup;
    }
}
