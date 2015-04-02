package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.bidding.TrumpsInHand;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.MULTIPLY;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.binary;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ABS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class NodeTest {

    static final int A_VALUE = 123;
    static final ConstantNode<Object, Object> CONST_NODE = new ConstantNode<>(A_VALUE);
    static final int ANOTHER_VALUE = 999;
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeTest.class);
    public static final BinaryNode<Object, Object> TWO_TIMES_MINUS_THREE = binary(MULTIPLY, constant(2), constant(-3));

    @Test
    public void replaceChildShouldThrowForInvalidIndex() {
        assertThatThrownBy(() -> TWO_TIMES_MINUS_THREE.replaceChild(constant(7), constant(77)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("doesn't have a child");
    }

    @Test
    public void replaceChildShouldThrowForNullReplacement() {
        assertThatThrownBy(() -> TWO_TIMES_MINUS_THREE.replaceChild(constant(7), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Replacement node");
    }

    @Test
    public void effectivelyConstantShouldReturnTrueForBinaryNodeOfConstants() {
        assertThat(TWO_TIMES_MINUS_THREE.effectivelyConstant()).isTrue();
    }

    @Test
    public void effectivelyConstantShouldReturnTrueForConstantishTree() {
        BinaryNode<Object, Object> node = binary(MULTIPLY, constant(2), unary(ABS, constant(-3)));
        assertThat(node.effectivelyConstant()).isTrue();
    }

    @Test
    public void effectivelyConstantShouldReturnTrue() {
        BinaryNode<Object, Object> node = binary(MULTIPLY, constant(2), unary(ABS, constant(-3)));
        assertThat(node.effectivelyConstant()).isTrue();
    }

    @Test
    public void effectivelyConstantShouldReturnFalseForRandomNode() {
        BinaryNode<Object, Object> node
                = binary(MULTIPLY, constant(2), unary(ABS, new RandomNode<>(mock(Random.class))));
        assertThat(node.effectivelyConstant()).isFalse();
    }

    @Test
    public void effectivelyConstantShouldReturnFalseForTrumpsInHand() {
        BinaryNode<Range, BidEvaluationContext> node
                = binary(MULTIPLY, constant(2), unary(ABS, new TrumpsInHand()));
        assertThat(node.effectivelyConstant()).isFalse();
    }

    @Test
    public void equalsTest() {
        assertThat(CONST_NODE).isEqualTo(new ConstantNode<>(A_VALUE));
        assertThat(CONST_NODE).isNotEqualTo(new ConstantNode<>(ANOTHER_VALUE));
        assertThat(CONST_NODE).isNotEqualTo(new RandomNode<>(mock(Random.class)));
        assertThat(CONST_NODE).isNotEqualTo(null);
    }

    @Test
    public void hashCodeShouldWork() {
        assertThat(CONST_NODE.hashCode()).isNotEqualTo(new ConstantNode<>(ANOTHER_VALUE).hashCode());
    }

    @Test
    public void hashCodeShouldBeUniqueAcrossSubClassesToo() {
        assertThat(CONST_NODE.hashCode()).isNotEqualTo(new RandomNode<>(mock(Random.class)).hashCode());
    }
}