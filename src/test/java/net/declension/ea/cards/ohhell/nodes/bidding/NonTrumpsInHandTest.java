package net.declension.ea.cards.ohhell.nodes.bidding;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.data.Range;
import net.declension.ea.cards.ohhell.nodes.Node;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static net.declension.ea.cards.ohhell.nodes.ConstantNode.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class NonTrumpsInHandTest {

    public static final Node<Range, BidEvaluationContext> DEFAULT = constant(999);
    private NonTrumpsInHand node;

    @Before
    public void setUp() throws Exception {
        node = new NonTrumpsInHand();
    }

    @Test
    public void simplifyShouldReturnDefaultIfSuitsOutOfRange() {
        node.setChildren(asList(constant(4), constant(7), DEFAULT));
        assertThat(node.simplifiedVersion()).isEqualTo(DEFAULT);
    }

    @Test
    public void simplifyShouldReturnDefaultIfIndexOutOfRange() {
        node.setChildren(asList(constant(3), constant(52), DEFAULT));
        assertThat(node.simplifiedVersion()).isEqualTo(DEFAULT);
    }

    @Test
    public void simplifyShouldReturnThisIfAllGood() {
        node.setChildren(asList(constant(3), constant(7), DEFAULT));
        assertThat(node.simplifiedVersion()).isNotEqualTo(DEFAULT);
    }
}