package net.declension.ea.cards.ohhell;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;

public class PreComputedFitnessEvaluator<T> implements FitnessEvaluator<T> {
    @Override
    public double getFitness(T candidate, List<? extends T> population) {
        throw new UnsupportedOperationException("This is a no-op fitness evaluator");
    }

    @Override
    public boolean isNatural() {
        // Ranking has lowest numbers as best.
        return false;
    }
}
