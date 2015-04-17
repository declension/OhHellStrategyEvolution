package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.BinaryNode;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.ea.cards.ohhell.nodes.NodeFactory;
import net.declension.games.cards.ohhell.BaseGameTest;
import org.junit.Before;
import org.junit.Test;
import org.uncommons.maths.random.Probability;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.DIVIDE;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.MULTIPLY;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ItemNode.item;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ABS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static org.assertj.core.api.Assertions.assertThat;

public class TreeMutationTest extends BaseGameTest {
    private TreeMutation mutator;

    @Before
    public void setUp() throws Exception {
        gameSetup = createDefaultGameSetup();
        mutator = new TreeMutation(Probability.ONE, new NodeFactory<>(new Random()));
    }

    @Test
    public void applyShouldMutate() {
        Node<Range, BidEvaluationContext> parent = new BinaryNode<>(DIVIDE);
        Node<Range, BidEvaluationContext> node = new BinaryNode<>(MULTIPLY);
        parent.addChild(node);
        parent.addChild(constant(5));
        Node<Range, BidEvaluationContext> child = constant(2);
        Node<Range, BidEvaluationContext> anotherChild =unary(ABS);
        anotherChild.addChild(constant(-3));
        node.setChildren(asList(child, anotherChild));
        GeneticStrategy strategy = new GeneticStrategy(gameSetup, parent);

        // Do mutation
        List<GeneticStrategy> results = mutator.apply(asList(strategy), new Random());
        assertThat(results).hasSize(1);

        GeneticStrategy mutated = results.get(0);
        assertThat(strategy.getBidEvaluator()).isNotEqualTo(mutated.getBidEvaluator());
    }

    @Test
    public void applyShouldMutateForTerminalsToo() {
        GeneticStrategy strategy = new GeneticStrategy(gameSetup, constant(5));

        // Do mutation
        List<GeneticStrategy> results = mutator.apply(asList(strategy), new Random());
        assertThat(results).hasSize(1);

        GeneticStrategy mutated = results.get(0);
        assertThat(strategy.getBidEvaluator()).isNotEqualTo(mutated.getBidEvaluator());
    }


    @Test
    public void applyShouldMutateForNonConstantTerminals() {
        GeneticStrategy strategy = new GeneticStrategy(gameSetup, item());

        // Do mutation
        List<GeneticStrategy> results = mutator.apply(asList(strategy), new Random());
        assertThat(results).hasSize(1);

        GeneticStrategy mutated = results.get(0);
        assertThat(strategy.getBidEvaluator()).isNotEqualTo(mutated.getBidEvaluator());
    }

    @Test
    public void applyShouldMutateForUnaryToo() {
        GeneticStrategy strategy = new GeneticStrategy(gameSetup, unary(ABS, constant(-5)));

        // Do mutation
        List<GeneticStrategy> results = mutator.apply(asList(strategy), new Random());
        assertThat(results).hasSize(1);

        GeneticStrategy mutated = results.get(0);
        Node<Range, BidEvaluationContext> bidEvaluator = strategy.getBidEvaluator();
        assertThat(bidEvaluator).isNotEqualTo(mutated.getBidEvaluator());
        assertThat(bidEvaluator.getNode(1)).isEqualTo(constant(-5));


    }
}