package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import org.junit.Test;

import java.util.Random;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.*;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.binary;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
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
        // set up a Multiple
        when(mockRng.nextInt(BinaryNode.Operator.ALL_BINARY_OPERATORS.size())).thenReturn(2);
        node.mutate(mockRng);
        assertThat(node.getOperator()).isEqualTo(MULTIPLY);
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
    public void simplifyShouldRemoveIdenticalSubtraction() {
        BinaryNode<Range, BidEvaluationContext> node = binary(SUBTRACT, new ItemNode(), new ItemNode());
        assertThat(node.simplifiedVersion()).isEqualTo(constant(0));
    }

    /**
     * Dummy context
     */
    private static class TestContext {
    }
}