package net.declension.games.cards.ohhell.player;

import java.util.Collection;

import static java.util.Arrays.asList;

public final class TestData {

    public static final Player ALICE = new DummyPlayer("Alice");
    public static final Player BOB = new DummyPlayer("Bob");
    public static final Player CHARLIE = new DummyPlayer("Charlie");
    public static final Player DANIELLE = new DummyPlayer("Danielle");
    public static final Collection<Player> PLAYERS = asList(ALICE, BOB, CHARLIE, DANIELLE);
}
