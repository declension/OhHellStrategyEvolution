package net.declension.ea.cards.ohhell.nodes;

import org.junit.Test;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.ADD;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.MULTIPLY;
import static net.declension.ea.cards.ohhell.nodes.ConstNode.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BinaryNodeTest {

    public static final TestContext TEST_CONTEXT = new TestContext();

    @Test
    public void evaluateShouldThrowForMissingChildren() {
        Node<TestContext> node = new BinaryNode<>(ADD);
        assertThatThrownBy(() -> node.evaluate(TEST_CONTEXT))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("0 children");
    }

    @Test
    public void evaluateShouldAddConstants() {
        Node<TestContext> node = new BinaryNode<>(ADD, constant(1), constant(2));
        assertThat(node.evaluate(TEST_CONTEXT)).isEqualTo(3.0);
        assertThat(node.toString()).isEqualTo("(1 + 2)");
    }


    @Test
    public void evaluateShouldEvaluateWholeTree() {
        Node<TestContext> node = new BinaryNode<>(ADD);
        Node<TestContext> subNode = new BinaryNode<TestContext>(MULTIPLY).setChildren(asList(constant(3), constant(4)));
        node.setChildren(asList(constant(1), subNode));
        assertThat(node.evaluate(TEST_CONTEXT)).isEqualTo(1.0 + (3 * 4));
        assertThat(node.toString()).isEqualTo("(1 + (3 * 4))");
    }


    /**
     * Dummy context
     */
    private static class TestContext {
    }
}