package net.declension.ea.cards.ohhell;

import net.declension.games.cards.Deck;
import net.declension.games.cards.ohhell.strategy.OhHellStrategy;
import net.declension.games.cards.ohhell.strategy.SimpleRandomOhHellStrategy;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class Evolver {

    public static final int NUMBER_OF_RUNS = 100_000;
    public static final int POPULATION_SIZE = 40;
    public static final int ELITE_COUNT = 2;
    public static final int MAX_RUNTIME_SECONDS = 180;


    private OhHellStrategy getWinningStrategy() {
        return new SimpleRandomOhHellStrategy(new MersenneTwisterRNG());
    }

    public static void main(String[] args) {
        Evolver evolver = new Evolver();
        System.out.println(new Deck().shuffled());

        System.out.println(evolver.getWinningStrategy());
    }
}
