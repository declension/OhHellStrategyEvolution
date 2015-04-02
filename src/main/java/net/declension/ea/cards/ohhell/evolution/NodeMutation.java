package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.uncommons.maths.random.Probability;

public class NodeMutation extends ModifyingOperator {

    /**
     * @param individualModificationProbability probability of an individual strategy being mutated at all
     * @param nodeModificationProbability       probability for each node in the "genome" being mutated.
     */
    public NodeMutation(Probability individualModificationProbability, Probability nodeModificationProbability) {
        super(individualModificationProbability, nodeModificationProbability);
    }

    @Override
    protected void modifyNode(Node<Range, BidEvaluationContext> node) {
        node.mutate(rng);
    }
}
