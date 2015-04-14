package net.declension.ea.cards.ohhell.evolution;

import net.declension.ea.cards.ohhell.GeneticStrategy;
import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.BinaryNode;
import net.declension.ea.cards.ohhell.nodes.Node;
import net.declension.games.cards.ohhell.GameSetup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.*;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ItemNode.item;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ABS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.uncommons.maths.random.Probability.ONE;

@RunWith(MockitoJUnitRunner.class)
public class TreeCrossoverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TreeCrossoverTest.class);
    @Mock
    private GameSetup gameSetup;

    @Mock
    private Random mockRandom;

    @Before
    public void setUp() throws Exception {
        when(gameSetup.getRNG()).thenReturn(mockRandom);
        when(mockRandom.nextInt()).thenReturn(0);
    }

    @Test
    public void mateHappyPath() {
        GeneticStrategy parent1 = createOneParent();
        GeneticStrategy parent2 = createDifferentParent();

        // Do mating
        TreeCrossover operator = new TreeCrossover(ONE);
        List<GeneticStrategy> parents = asList(parent1, parent2);
        LOGGER.info("Parents have {} and {}", parent1.getBidEvaluator(), parent2.getBidEvaluator());
        List<GeneticStrategy> results = operator.apply(parents, gameSetup.getRNG());
        assertThat(results).hasSize(2);
        assertThat(results).doesNotContainAnyElementsOf(parents);

        // Remember shuffle() occurs first in the crossover base.
        // Err, I'm sure this makes sense really.
        Node<Range, BidEvaluationContext> firstChildBidNode = results.get(0).getBidEvaluator();
        assertThat(firstChildBidNode.getNode(1)).isEqualTo(parent1.getBidEvaluator().getNode(1));
        assertThat(firstChildBidNode.getNode(5)).isEqualTo(parent2.getBidEvaluator().getNode(4));

        // Oh, the madness of trees
        Node<Range, BidEvaluationContext> secondChildBidNode = results.get(1).getBidEvaluator();
        assertThat(secondChildBidNode.getNode(1)).isEqualTo(parent2.getBidEvaluator().getNode(1));
        assertThat(secondChildBidNode.getNode(4)).isEqualTo(constant(5));
    }

    @Test
    public void matingShouldDoNothingForVerySimpleNodes() {
        GeneticStrategy complexParent = createOneParent();
        GeneticStrategy simpleParent = new GeneticStrategy(gameSetup, item());

        // Do mating
        TreeCrossover operator = new TreeCrossover(ONE);
        List<GeneticStrategy> parents = asList(complexParent, simpleParent);
        List<GeneticStrategy> results = operator.apply(parents, gameSetup.getRNG());
        assertThat(results).hasSize(2);
        assertThat(results).containsAll(parents);
    }

    private GeneticStrategy createOneParent() {
        Node<Range, BidEvaluationContext> divideNode = new BinaryNode<>(DIVIDE);
        Node<Range, BidEvaluationContext> multiplyNode = new BinaryNode<>(MULTIPLY);
        divideNode.addChild(multiplyNode);
        divideNode.addChild(constant(5));
        Node<Range, BidEvaluationContext> child = constant(2);
        Node<Range, BidEvaluationContext> anotherChild =unary(ABS);
        anotherChild.addChild(constant(-3));
        multiplyNode.setChildren(asList(child, anotherChild));

        return new GeneticStrategy(gameSetup, divideNode);
    }

    private GeneticStrategy createDifferentParent() {
        Node<Range, BidEvaluationContext> root = new BinaryNode<>(ADD);
        Node<Range, BidEvaluationContext> multiplyNode = new BinaryNode<>(MULTIPLY);
        multiplyNode.setChildren(asList(constant(6), constant(111)));
        root.addChild(multiplyNode);
        Node<Range, BidEvaluationContext> expNode = new BinaryNode<>(EXPONENTIATE);
        root.addChild(expNode);
        Node<Range, BidEvaluationContext> child = constant(2);
        expNode.setChildren(asList(child, item()));

        return new GeneticStrategy(gameSetup, root);
    }
}