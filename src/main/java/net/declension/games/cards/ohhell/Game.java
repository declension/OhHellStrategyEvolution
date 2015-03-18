package net.declension.games.cards.ohhell;

import com.google.common.collect.Lists;
import net.declension.collections.ImmutableCircularList;
import net.declension.collections.SlotsMap;
import net.declension.games.cards.Card;
import net.declension.games.cards.Deck;
import net.declension.games.cards.Suit;
import net.declension.games.cards.ohhell.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static net.declension.collections.CollectionUtils.ADD_NULLABLE_INTEGERS;

/***
 * The entry point for playing a game.
 */
public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<Player> players;
    private Player dealer;
    private final GameSetup setup;

    private Suit trumps;
    private BidValidator bidValidator;
    private Map<Player, Integer> tricksTaken;
    private AllBids tricksBid;

    private List<Map<Player, Integer>> scoreSheet;

    private class SetTrickLeadSuitFirstCardListener implements FirstCardListener<Trick> {
        @Override
        public void onFirstCard(Trick trick, Card firstCard) {
            LOGGER.debug("Leading suit is {}, trumps are {}", firstCard.suit(), trumps);
            trick.setCardOrdering(setup.createTrickComparator(getTrumps(), firstCard.suit()));
        }
    }

    /**
     * Construct a game, ready for playing.
     */
    public Game(List<Player> players, GameSetup setup, Player dealer) {
        this.players = new ImmutableCircularList<>(players);
        this.setup = setup;
        this.dealer = dealer;
        LOGGER.warn("Setting up {} players for this game: {}", players.size(), players);
    }

    /**
     * Play an entire game as set up from the constructor.
     * TODO: more output / listeners
     */
    public Map<Player, Integer> play() {
        scoreSheet = setup.getRoundsProducer().map(this::playRound).collect(toList());
        LOGGER.debug("Scoresheet: {}", scoreSheet);

        Stream<Map.Entry<Player, Integer>> flatStream = scoreSheet.stream()
                .flatMap(map -> map.entrySet().stream());

        Map<Player, Integer> scores = flatStream
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, ADD_NULLABLE_INTEGERS));

        LOGGER.info("Final scores: {}", scores);
        return scores;
    }

    Map<Player, Integer> playRound(Integer handSize) {
        LOGGER.info("============== Player {} is dealing {} card(s) to each player ==============", dealer, handSize);
        Deck deck = new Deck().shuffled();
        trumps = deck.pullTopCard().suit();
        deal(handSize, deck, players);
        LOGGER.info("Trumps are {}", trumps);

        bidValidator = new BidBustingRulesBidValidator(handSize);
        takeBids(handSize);
        Player startingPlayer = getNextDealer();

        tricksTaken = new SlotsMap<>(players, 0);
        while (dealer.hasCards()) {
            startingPlayer = playTrickStartingWith(startingPlayer);
            tricksTaken.put(startingPlayer, tricksTaken.get(startingPlayer) + 1);
        }

        LOGGER.info("Tricks taken: {}", tricksTaken);
        dealer = getNextDealer();

        return players.parallelStream()
                .collect(toConcurrentMap(identity(), this::getScoreForPlayer));

    }

    private Integer getScoreForPlayer(Player player) {
        return setup.getScorer().scoreFor(tricksBid.get(player), tricksTaken.get(player));
    }

    private Player getNextDealer() {
        return players.get(getNextDealerIndex());
    }

    private Player playTrickStartingWith(Player starter) {
        LOGGER.info("==== new trick led by {} ====", starter);
        Trick trickSoFar = new Trick(players, new SetTrickLeadSuitFirstCardListener());
        roundTheTableFrom(starter, player -> {
            Card card = player.playCard(this, trickSoFar);
            checkForNullCardFrom(player, card);
            trickSoFar.put(player, card);
            LOGGER.info("{} played {}", player, card);
        });
        Player winner= trickSoFar.winningPlayer();
        LOGGER.info("{} won that trick with {}.", winner, trickSoFar.get(winner));
        return winner;
    }

    private void roundTheTableAfterDealer(Consumer<Player> playerConsumer) {
        players.listIterator(getNextDealerIndex()).forEachRemaining(playerConsumer);
    }

    private void roundTheTableFrom(Player player, Consumer<Player> playerConsumer) {
        players.listIterator(players.indexOf(player)).forEachRemaining(playerConsumer);
    }

    private void checkForNullCardFrom(Player player, Card card) {
        if (card == null) {
            throw new IllegalStateException(
                    format("%s tried to play a null card (using %s)", player, player.getStrategy()));
        }
    }

    private void takeBids(Integer handSize) {
        tricksBid = new AllBids(players);
        roundTheTableAfterDealer(player -> {
            Integer bid = player.bid(this, tricksBid);
            tricksBid.put(player, bid);
            doubleCheckBids(handSize, tricksBid, player);
        });
        int total = tricksBid.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.info("Here are the {} bids totalling {} (for {} tricks): {}",
                    tricksBid.size(), total, handSize, tricksBid);
    }

    private void doubleCheckBids(Integer handSize, AllBids bids, Player player) {
        if (!bidValidator.test(bids)) {
            throw new IllegalStateException(
                    format("Oh dear: %s had tried a dodgy bid of %d for a trick of size %d. The rest: %s",
                            player, bids.get(player), handSize, bids));
        }
    }

    private int getNextDealerIndex() {
        return players.indexOf(dealer) + 1;
    }

    private void deal(Integer number, Deck deck, Collection<? extends Player> players) {
        List<Card> dealtCards = deck.pullCards(players.size() * number);

        // It's fully random, so don't have to deal one card at a time - just give n cards to each player
        giveHandsToPlayers(players, Lists.partition(dealtCards, number));

        LOGGER.debug("After dealing: {}", deck);
    }

    /**
     * A would-be {@code zip} method.
     * @param players the players
     * @param hands the list of hands to be given out, <strong>in the same order</strong>.
     */
    private void giveHandsToPlayers(Collection<? extends Player> players, List<List<Card>> hands) {
        Iterator<? extends Player> itr = players.iterator();
        for (int i =0; i < players.size(); i++) {
            itr.next().receiveNewHand(trumps, hands.get(i));
        }
    }

    public Suit getTrumps() {
        return trumps;
    }

    public BidValidator getBidValidator() {
        return bidValidator;
    }

    public Map<? extends Player,Integer> getTricksBid() {
        return tricksBid;
    }

    public Map<? extends Player, Integer> getTricksTaken() {
        return tricksTaken;
    }

    public List<Map<Player, Integer>> getScoreSheet() {
        return scoreSheet;
    }

    @Override
    public String toString() {
        return "Game{" + "players=" + players + ", dealer=" + dealer + ", setup=" + setup + ", trumps="
                + trumps + ", bidValidator=" + bidValidator + '}';
    }

}
