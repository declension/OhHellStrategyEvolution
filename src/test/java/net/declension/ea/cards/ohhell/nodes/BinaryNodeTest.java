package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import org.junit.Test;

import java.util.Random;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.*;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.binary;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.deadNumber;
import static net.declension.ea.cards.ohhell.nodes.ItemNode.item;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BinaryNodeTest {

    public static final TestContext TEST_CONTEXT = new TestContext();
    private static final Number DUMMY_ITEM = mock(Number.class);

    @Test
    public void evaluateShouldThrowForMissingChildren() {
        Node<Number, TestContext> node = new BinaryNode<>(ADD);
        assertThatThrownBy(() -> node.evaluate(DUMMY_ITEM, TEST_CONTEXT))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("0 children");
    }

    @Test
    public void evaluateShouldAddConstants() {
        Node<Number, TestContext> node = binary(ADD, constant(1), constant(2));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(3.0);
        assertThat(node.toString()).isEqualTo("(1 + 2)");
    }


    @Test
    public void evaluateShouldEvaluateWholeTree() {
        Node<Number, TestContext> node = new BinaryNode<>(ADD);
        Node<Number, TestContext> subNode = new BinaryNode<>(MULTIPLY);
        subNode.setChildren(asList(constant(3), constant(4)));
        node.setChildren(asList(constant(1), subNode));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(1.0 + (3 * 4));
        assertThat(node.toString()).isEqualTo("(1 + (3 * 4))");
    }

    @Test
    public void exponentiateWorksToo() {
        Node<Number, TestContext> node = binary(EXPONENTIATE, constant(2), constant(3));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(8.0);
        assertThat(node.toString()).isEqualTo("(2 ^ 3)");
    }

    @Test
    public void integerDivideProducesDouble() {
        Node<Number, TestContext> node = binary(DIVIDE, constant(3), constant(2));
        assertThat(node.evaluate(DUMMY_ITEM, TEST_CONTEXT)).isEqualTo(1.5);
        assertThat(node.toString()).isEqualTo("(3 / 2)");
    }

    @Test
    public void mutatedShouldProduceNewOperator() {
        BinaryNode<Number, TestContext> node = binary(EXPONENTIATE, constant(2), constant(3));
        Random mockRng = mock(Random.class);
        when(mockRng.nextInt(2)).thenReturn(1);
        // set up a Multiply
        when(mockRng.nextInt(BinaryNode.Operator.ALL_BINARY_OPERATORS.size())).thenReturn(2);
        node.mutate(mockRng);
        assertThat(node.getOperator()).isEqualTo(MULTIPLY);
    }

    @Test
    public void mutatedShouldSwapChildren() {
        BinaryNode<Number, TestContext> node = binary(EXPONENTIATE, constant(2), constant(3));
        Random mockRng = mock(Random.class);
        node.mutate(mockRng);
        assertThat(node).isEqualTo(binary(EXPONENTIATE, constant(3), constant(2)));
    }

    @Test
    public void simplifyShouldReduceAdds() {
        BinaryNode<Range, BidEvaluationContext> node = binary(ADD, constant(2), constant(3));
        assertThat(node.simplifiedVersion()).isEqualTo(constant(5));
    }

    @Test
    public void simplifyShouldReduceMultiplies() {
        BinaryNode<Range, BidEvaluationContext> node = binary(MULTIPLY, constant(2.0), constant(3));
        assertThat(node.simplifiedVersion()).isEqualTo(constant(6.0));
    }

    @Test
    public void simplifyShouldReduceDivides() {
        BinaryNode<Range, BidEvaluationContext> node = binary(DIVIDE, constant(12.0), constant(3));
        assertThat(node.simplifiedVersion()).isEqualTo(constant(4.0));
    }

    @Test
    public void simplifyShouldFlipDoubleNegatives() {
        BinaryNode<Range, BidEvaluationContext> node = binary(SUBTRACT, item(), constant(-3));
        assertThat(node.toString()).isEqualTo("(x - -3)");
        assertThat(node.simplifiedVersion().toString()).isEqualTo("(x + 3)");
    }

    @Test
    public void simplifyShouldRemoveIdenticalSubtraction() {
        BinaryNode<Range, BidEvaluationContext> node = binary(SUBTRACT, item(), new ItemNode());
        assertThat(node.simplifiedVersion()).isEqualTo(constant(0));
    }

    @Test
    public void simplifyShouldReplaceDivisionBySelfWithOne() {
        BinaryNode<Range, BidEvaluationContext> node = binary(DIVIDE, item(), new ItemNode());
        assertThat(node.simplifiedVersion()).isEqualTo(constant(1));
    }

    @Test
    public void simplifyShouldReplaceDivisionByZeroWithNan() {
        BinaryNode<Range, BidEvaluationContext> node = binary(DIVIDE, item(), constant(0));
        assertThat(node.simplifiedVersion()).isEqualTo(deadNumber());
    }

    @Test
    public void simplifyShouldReplaceMultiplicationByOneWithSelf() {
        BinaryNode<Range, BidEvaluationContext> node = binary(MULTIPLY, item(), constant(1));
        assertThat(node.simplifiedVersion()).isEqualTo(item());
        node = binary(MULTIPLY, constant(1), item());
        assertThat(node.simplifiedVersion()).isEqualTo(item());
    }


    /**
     * Dummy context
     */
    private static class TestContext {
    }
}