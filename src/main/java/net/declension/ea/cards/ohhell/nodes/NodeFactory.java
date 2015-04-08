package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.BidEvaluationContext;
import net.declension.ea.cards.ohhell.nodes.bidding.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedList;
import static net.declension.collections.CollectionUtils.pickRandomly;
import static net.declension.ea.cards.ohhell.data.Aggregator.ALL_AGGREGATORS;
import static net.declension.ea.cards.ohhell.nodes.AggregatingNode.aggregator;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.ALL_BINARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ALL_UNARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static net.declension.ea.cards.ohhell.nodes.bidding.AggregatedBiddingData.aggregatedBiddingData;

public class NodeFactory<I, C extends BidEvaluationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);

    static final int MAX_ARITY = 6;
    static final int MAX_INT_RANGE = 100;

    private final Random rng;
    private final Map<Supplier<Node<I, C>>, Integer> weightedNodeMap;

    public NodeFactory(Random rng) {
        this.rng = rng;
        weightedNodeMap = createBiddingNodeMap();
    }

    public Node<I, C> createEphemeralRandom() {
        return new ConstantNode<>(rng.nextDouble());
    }

    public Node<I, C> createEphemeralIntegerRandom() {
        return new ConstantNode<>(rng.nextInt(MAX_INT_RANGE) - MAX_INT_RANGE / 2);
    }


    public static <I, C> Node<I, C> createBinary(BinaryNode.Operator op, Node<I, C> left, Node<I, C> right) {
        BinaryNode<I, C> ret = new BinaryNode<>(op);
        return ret.setChildren(asList(left, right));
    }

    public Node<I, C> createRandomTree(int maxDepth) {
        if (maxDepth == 0) {
            return createTerminalNode();
        }
        Node<I, C> node = createRawRandomNode();
        List<Node<I, C>> children = synchronizedList(new ArrayList<>());
        Integer arity = arityForNode(node);
        // Allow nodes to be partially completed trees...
        IntStream.rangeClosed(node.children().size() + 1, arity)
                 .forEach(i -> children.add(createRandomTree(maxDepth - 1)));
        node.setChildren(children);
        return node;
    }

    private Integer arityForNode(Node<I, C> node) {
        // Remember, optionals generally mean lists, and there's not much point building them with < 2.
        return node.arity().orElse(rng.nextInt(MAX_ARITY - 2) + 2);
    }

    private Node<I, C> createTerminalNode() {
        return (rng.nextInt(2)==1)? createEphemeralRandom() : createEphemeralIntegerRandom();
    }

    private Node<I, C> createRawRandomNode() {
        // TODO: weighted choosing.
        return (Node<I, C>) pickRandomly(rng, weightedNodeMap.keySet()).get();
    }

    private Map<Supplier<Node<I, C>>, Integer> createBiddingNodeMap() {
        Map<Supplier<Node<I, C>>, Integer> suppliers = new HashMap<>();

        suppliers.put(this::createEphemeralRandom, 1);
        suppliers.put(this::createEphemeralIntegerRandom, 1);
        ALL_BINARY_OPERATORS.stream()
                .forEach(op -> suppliers.put(() -> binary(op), 1));
        ALL_UNARY_OPERATORS.stream()
                .forEach(op -> suppliers.put(() -> unary(op), 1));
        ALL_AGGREGATORS.stream()
                      .forEach(op -> suppliers.put(() -> aggregator(op), 1));
        suppliers.put(() -> new RandomNode(rng), 1);

        addBiddingNodeSuppliers(suppliers);
        addAggregatedBidNodeSuppliers(suppliers);
        return suppliers;
    }

    private void addBiddingNodeSuppliers(Map<Supplier<Node<I, C>>, Integer> suppliers) {
        suppliers.put(() -> (Node<I, C>) new RemainingBidNode(), 1);
        suppliers.put(() -> (Node<I, C>) new ItemNode(), 1);
        suppliers.put(() -> (Node<I, C>) new BidsSoFar(), 1);
        suppliers.put(() -> (Node<I, C>) new HandSize(), 1);
        suppliers.put(() -> (Node<I, C>) new TrumpsInHand(), 1);
    }

    private void addAggregatedBidNodeSuppliers(Map<Supplier<Node<I, C>>, Integer> suppliers) {
        ALL_AGGREGATORS.stream()
                       .forEach(ag -> suppliers.put(() -> (Node<I, C>) aggregatedBiddingData(ag), 1));
    }

    private static <I,C>  BinaryNode<I, C> binary(BinaryNode.Operator op) {
        return new BinaryNode<>(op);
    }

}
