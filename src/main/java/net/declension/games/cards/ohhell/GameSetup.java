package net.declension.games.cards.ohhell;

import org.uncommons.maths.random.MersenneTwisterRNG;

import java.util.Random;

public class GameSetup {
    private final Random random = new MersenneTwisterRNG();

    public Random getRNG() {
        return random;
    }
}
