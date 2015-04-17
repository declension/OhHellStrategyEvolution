package net.declension.ea.cards.ohhell.evolution;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class PreComputedFitnessEvaluator<T> implements FitnessEvaluator<T> {

    public static final boolean IS_NATURAL = true;

    @Override
    public double getFitness(T candidate, List<? extends T> population) {
        throw new UnsupportedOperationException("This is a no-op fitness evaluator");
    }

    @Override
    public boolean isNatural() {
        // Scores: higher is better
        return IS_NATURAL;
    }
}
