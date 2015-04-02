package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ABS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static org.assertj.core.api.Assertions.assertThat;

public class NonTrumpsInHandTest {

    public static final Node<Range, BidEvaluationContext> DEFAULT = constant(999);
    public static final Node<Range, BidEvaluationContext>
            OUTSIDE = constant(52);
    public static final Node<Range, BidEvaluationContext>
            INSIDE_SUIT = constant(3);
    private NonTrumpsInHand node;

    @Before
    public void setUp() {
        node = new NonTrumpsInHand();
    }

    @Test
    public void simplifyShouldReturnDefaultIfSuitsOutOfRange() {
        node.setChildren(asList(constant(4), constant(7), DEFAULT));
        assertThat(node.simplifiedVersion()).isEqualTo(DEFAULT);
    }

    @Test
    public void simplifyShouldReturnDefaultIfIndexOutOfRange() {
        node.setChildren(asList(INSIDE_SUIT, OUTSIDE, DEFAULT));
        assertThat(node.simplifiedVersion()).isEqualTo(DEFAULT);
    }

    @Test
    public void simplifyShouldReturnThisIfAllGood() {
        node.setChildren(asList(INSIDE_SUIT, constant(7), DEFAULT));
        assertThat(node.simplifiedVersion()).isNotEqualTo(DEFAULT);
    }

    @Test
    public void simplifyShouldReturnSimplifiedTreeIfOutside() {
        node.addChild(INSIDE_SUIT);
        node.addChild(OUTSIDE);
        node.addChild(unary(ABS, constant(-4)));
        assertThat(node.simplifiedVersion()).isEqualTo(constant(4));
    }

    @Test
    public void simplifyShouldReturnNodeWithSimplifiedChildrenIfInside() {
        node.addChild(unary(ABS, constant(-3)));
        node.addChild(unary(ABS, constant(-9)));
        node.addChild(unary(ABS, constant(-1234)));
        Node<Range, BidEvaluationContext> simple = node.simplifiedVersion();
        assertThat(simple.child(0)).isEqualTo(constant(3));
        assertThat(simple.child(1)).isEqualTo(constant(9));
        assertThat(simple.child(2)).isEqualTo(constant(1234));
    }

}