package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.uncommons.maths.random.Probability;

public class TreeMutation extends ModifyingOperator {

    /**
     * @param treeModificationProbability   probability of an individual strategy being mutated at all
     * @param nodeModificationProbability   probability for each node in the "genome" being mutated.
     */
    public TreeMutation(Probability treeModificationProbability, Probability nodeModificationProbability) {
        super(treeModificationProbability, nodeModificationProbability);
    }

    @Override
    protected void modifyNode(Node<Range, BidEvaluationContext> node) {
        node.mutate(rng);
    }
}
