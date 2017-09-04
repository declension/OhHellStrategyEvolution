package net.declension.ea.cards.ohhell.evolution;

import net.declension.games.cards.ohhell.GameSetup;
import net.declension.games.cards.ohhell.strategy.*;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.shuffle;
import static net.declension.utils.Validation.requireNonNullParam;

/**
 * Creates strategies with random bid-evaluation trees.
 */
public class AlgorithmicStrategyFactory extends AbstractCandidateFactory<OhHellStrategy> {

    private final GameSetup gameSetup;

    public AlgorithmicStrategyFactory(GameSetup gameSetup) {
        requireNonNullParam(gameSetup, "Game Setup");
        this.gameSetup = gameSetup;
    }

    /**
     * Note this implementation ignores the supplied rng, in favour of one set up from the {@link GameSetup}
     */
    @Override
    public OhHellStrategy generateRandomCandidate(Random rng) {
        return createStrategies(1).get(0);
    }

    public List<OhHellStrategy> createStrategies(int number) {
        List<OhHellStrategy> shortList = asList(new AverageSimpleStrategy(gameSetup),
                                                new AverageRandomStrategy(gameSetup.getRNG()),
                                                new TrumpsBasedRandomStrategy(gameSetup.getRNG()),
                                                new TrumpsBasedSimpleStrategy(gameSetup)
        );
        shuffle(shortList, gameSetup.getRNG());
        return shortList.subList(0, number);
    }
}
