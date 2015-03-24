package net.declension.games.cards.ohhell;

import net.declension.games.cards.ohhell.player.BasicPlayer;
import net.declension.games.cards.ohhell.player.Player;
import net.declension.games.cards.ohhell.player.PlayerID;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseGameTest {
    public static final int ID_SIZE = 4;
    protected Game game;
    // We need this to be BasicPlayer to allow a little cheating in the test..
    protected List<Player> players;
    protected GameSetup gameSetup;

    public static List<Player> generatePlayers(int numPlayers, OhHellStrategy strategy, GameSetup gameSetup) {
        return IntStream.rangeClosed(1, numPlayers)
                .mapToObj(num -> new BasicPlayer(createPlayerID(strategy), gameSetup, strategy))
                .collect(toList());
    }

    private static PlayerID createPlayerID(OhHellStrategy strategy) {
        return new PlayerID(format("%" + ID_SIZE + "s-%s",
                UUID.randomUUID().toString().substring(0, ID_SIZE), strategy.getName()));
    }

    protected int totalScores() {
        return game.getTricksTaken().values().stream().mapToInt(Integer::intValue).sum();
    }

    protected void assertThatNobodyHasCards() {
        players.forEach(player -> assertThat(((BasicPlayer) player).peekAtHand()).isEmpty());
    }

    protected GameSetup createDefaultGameSetup(int handSize) {
        return new GameSetup(() -> IntStream.rangeClosed(1, handSize).boxed());
    }
}
