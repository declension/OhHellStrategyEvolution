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

    public static final Node<Range, BidEvaluationContext> OUTSIDE = constant(52);
    public static final Node<Range, BidEvaluationContext> INSIDE_SUIT = constant(3);
    public static final Node<Range, BidEvaluationContext> OUTSIDE_SUIT = constant(4);
    public static final Node<Range, BidEvaluationContext> INSIDE_RANK = constant(7);
    public static final Node<Object, Object> DEAD = constant(Double.NaN);
    private NonTrumpsInHand node;

    @Before
    public void setUp() {
        node = new NonTrumpsInHand();
    }

    @Test
    public void simplifyShouldReturnDeadIfSuitsOutOfRange() {
        node.setChildren(asList(OUTSIDE_SUIT, INSIDE_RANK));
        assertThat(node.simplifiedVersion()).isEqualTo(DEAD);
    }

    @Test
    public void simplifyShouldReturnDeadIfIndexOutOfRange() {
        node.setChildren(asList(INSIDE_SUIT, OUTSIDE));
        assertThat(node.simplifiedVersion()).isEqualTo(DEAD);
    }

    @Test
    public void simplifyShouldReturnThisIfAllGood() {
        node.setChildren(asList(INSIDE_SUIT, INSIDE_RANK));
        assertThat(node.simplifiedVersion()).isNotEqualTo(DEAD);
    }

    @Test
    public void simplifyShouldReturnSimplifiedTreeIfOutside() {
        node.addChild(INSIDE_SUIT);
        node.addChild(OUTSIDE);
        node.addChild(unary(ABS, constant(-4)));
        assertThat(node.simplifiedVersion()).isEqualTo(DEAD);
    }

    @Test
    public void simplifyShouldReturnNodeWithSimplifiedChildrenIfInside() {
        node.addChild(unary(ABS, constant(-3)));
        node.addChild(unary(ABS, constant(-9)));
        Node<Range, BidEvaluationContext> simple = node.simplifiedVersion();
        assertThat(simple.child(0)).isEqualTo(constant(3));
        assertThat(simple.child(1)).isEqualTo(constant(9));
    }

}