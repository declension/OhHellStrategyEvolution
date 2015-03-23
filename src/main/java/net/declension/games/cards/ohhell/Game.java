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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static net.declension.collections.CollectionUtils.ADD_NULLABLE_INTEGERS;
import static net.declension.collections.CollectionUtils.comparingEntriesByDescendingValues;
import static net.declension.utils.OptionalUtils.optionalToString;

/***
 * The entry point for playing a game.
 */
public class Game {
    private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

    private final List<Player> players;
    private Player dealer;
    private final GameSetup setup;

    private Optional<Suit> trumps;
    private BidValidator bidValidator;
    private Map<Player, Integer> tricksTaken;
    private AllBids tricksBid;

    private List<Map<Player, Integer>> scoreSheet;

    private class SetTrickOrderingFirstCardListener implements FirstCardListener<Trick> {
        @Override
        public void onFirstCard(Trick trick, Card firstCard) {
            LOGGER.debug("Leading suit is {}, trumps are {}", firstCard.suit(), optionalToString(trumps));
            trick.setCardOrdering(setup.createTrickScoringComparator(getTrumps(), Optional.of(firstCard.suit())));
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
     *
     * @return An ordered list of {@link java.util.Map.Entry} of {@link Player} to their score, winner first.
     *
     * TODO: more output / listeners
     */
    public List<Map.Entry<Player, Integer>> play() {
        scoreSheet = setup.getRoundsProducer().map(this::playRound).collect(toList());
        LOGGER.debug("Scoresheet: {}", scoreSheet);

        LOGGER.info("Final scores: {}", getScoresFromScoreSheet());
        return getScoresFromScoreSheet();
    }

    private List<Map.Entry<Player, Integer>> getScoresFromScoreSheet() {
        // Use flatMap to unravel all the lines from all the sheet
        Stream<Map.Entry<Player, Integer>> flatStream = scoreSheet.stream()
                .flatMap(map -> map.entrySet().stream());
        // ...then use toMap()'s merge feature to allow us to add these across all games,
        // since it's an independent sum reduction across these entries per Player.
        Map<Player, Integer> scores = flatStream
                                              .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, ADD_NULLABLE_INTEGERS));
        return scores.entrySet().stream()
                     .sorted(comparingEntriesByDescendingValues())
                     .collect(toList());
    }

    Map<Player, Integer> playRound(Integer handSize) {
        LOGGER.info("============== Player {} is dealing {} card(s) to each player ==============", dealer, handSize);
        Deck deck = new Deck().shuffled();
        trumps = Optional.of(deck.pullTopCard().suit());
        deal(handSize, deck, players);
        LOGGER.info("Trumps are {}", optionalToString(trumps));

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
        return setup.getScorer().scoreFor(tricksBid.get(player).get(), tricksTaken.get(player));
    }

    private Player getNextDealer() {
        return players.get(getNextDealerIndex());
    }

    private Player playTrickStartingWith(Player starter) {
        LOGGER.info("==== new trick led by {} ====", starter.getID());
        Trick trickSoFar = new Trick(players, new SetTrickOrderingFirstCardListener());
        roundTheTableFrom(starter, player -> {
            Card card = player.playCard(this, trickSoFar);
            checkForNullCardFrom(player, card);
            trickSoFar.put(player, Optional.of(card));
            LOGGER.info("{} played {}", player.getID(), card);
        });
        Player winner= trickSoFar.winningPlayer().get();
        LOGGER.info("{} won that trick with {}.", winner, trickSoFar.get(winner).get());
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
        int total = tricksBid.values().stream()
                             .mapToInt(checkForMissingBid())
                             .sum();
        LOGGER.info("Here are the {} bids totalling {} (for {} tricks): {}",
                    tricksBid.size(), total, handSize, tricksBid);
    }

    private ToIntFunction<Optional<Integer>> checkForMissingBid() {
        return opt -> opt.orElseThrow(() -> new IllegalStateException("Missing bid value: " + tricksBid));
    }

    private void doubleCheckBids(Integer handSize, AllBids bids, Player player) {
        if (!bidValidator.test(bids)) {
            throw new IllegalStateException(
                    format("Oh dear: %s had tried a dodgy bid of %d for a trick of size %d. The rest: %s",
                            player, bids.get(player).get(), handSize, bids));
        }
    }

    private int getNextDealerIndex() {
        return players.indexOf(dealer) + 1;
    }

    private void deal(Integer number, Deck deck, Collection<? extends Player> players) {
        if (deck.size() < players.size() * number) {
            throw new IllegalArgumentException(format("Can't deal %d player(s) %d card(s) each for a deck of size %d",
                                                      players.size(), number, deck.size()));
        }
        List<Card> dealtCards = deck.pullCards(players.size() * number);

        // It's fully random, so don't have to deal one card at a time - just give n cards to each player
        giveHandsToPlayers(players, Lists.partition(dealtCards, number));
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

    public Optional<Suit> getTrumps() {
        return trumps;
    }

    public BidValidator getBidValidator() {
        return bidValidator;
    }

    public Map<Player,Integer> getFinalTricksBid() {
        return tricksBid.getFinalBids();
    }

    public Map<Player, Integer> getTricksTaken() {
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
