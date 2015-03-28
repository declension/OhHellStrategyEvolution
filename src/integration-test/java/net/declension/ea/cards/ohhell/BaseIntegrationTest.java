package net.declension.ea.cards.ohhell;

import net.declension.games.cards.ohhell.BaseGameTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.Probability;

public class BaseIntegrationTest extends BaseGameTest {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final int POPULATION_SIZE = 7;
    protected static final int ELITE_COUNT = 2;
    protected static final int MAX_RUNTIME_SECONDS = 5;
    protected static final int GAMES_PER_TOURNAMENT = 30;
    protected static final Probability REPLACEMENT_PROBABILITY = new Probability(0.1);
    protected static final int MAX_HAND_SIZE = 51 / POPULATION_SIZE + 2;
}
