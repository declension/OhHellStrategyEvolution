package net.declension.ea.cards.ohhell.nodes;

import net.declension.ea.cards.ohhell.data.InGameEvaluationContext;
import net.declension.ea.cards.ohhell.nodes.bidding.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.synchronizedList;
import static net.declension.collections.CollectionUtils.pickRandomly;
import static net.declension.ea.cards.ohhell.data.Aggregator.ALL_AGGREGATORS;
import static net.declension.ea.cards.ohhell.nodes.AggregatingNode.aggregator;
import static net.declension.ea.cards.ohhell.nodes.BinaryNode.Operator.ALL_BINARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.Operator.ALL_UNARY_OPERATORS;
import static net.declension.ea.cards.ohhell.nodes.UnaryNode.unary;
import static net.declension.ea.cards.ohhell.nodes.bidding.AggregatedRankData.aggregatedRankData;

public class NodeFactory<I, C extends InGameEvaluationContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);

    static final int MAX_ARITY = 6;
    static final int MAX_INT_RANGE = 6;

    private final Random rng;
    private final Map<Supplier<Node<I, C>>, Integer> weightedNodeMap;
    private Map<Supplier<Node<I, C>>, Integer> terminalSuppliers;
    private Map<Supplier<Node<I, C>>, Integer> nonTerminalSuppliers;

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


    public Node<I, C> createRandomTree(int minDepth, int maxDepth) {
        if (minDepth > maxDepth) {
            throw new IllegalArgumentException("Can't have min > max");
        }
        Node<I, C> ret = createRandomTree(0, minDepth, maxDepth);
        // TODO: remove this break-glass, once the depth solution is more reliable...
        int depth = ret.depth();
        if (depth < minDepth || depth > maxDepth) {
            throw new IllegalStateException(format("Depth has somehow become %d when limits are (%d, %d): %s", depth,
                                                   minDepth, maxDepth, ret));
        }
        return ret;
    }

    private Node<I, C> createRandomTree(int depth, int minDepth, int maxDepth) {

        if (depth == maxDepth ) {
            return createTerminalNode();
        }
        Node<I, C> node = (depth < minDepth)? createRawNonTerminalNode() : createRawRandomNode();
        List<Node<I, C>> children = synchronizedList(new ArrayList<>());
        // Allow nodes to be partially completed trees...
        IntStream.rangeClosed(node.children().size() + 1, arityForNode(node))
                 .forEach(i -> children.add(createRandomTree(depth + 1, minDepth, maxDepth)));
        node.setChildren(children);
        return node;
    }

    private Integer arityForNode(Node<I, C> node) {
        // Remember, optionals generally mean lists, and there's not much point building them with < 2.
        return node.arity().orElse(rng.nextInt(MAX_ARITY - 2) + 2);
    }

    private Node<I, C> createTerminalNode() {
        return pickRandomly(rng, terminalSuppliers.keySet()).get();
    }

    private Node<I, C> createRawNonTerminalNode() {
        return pickRandomly(rng, nonTerminalSuppliers.keySet()).get();
    }

    private Node<I, C> createRawRandomNode() {
        // TODO: weighted choosing.
        return pickRandomly(rng, weightedNodeMap.keySet()).get();
    }

    private Map<Supplier<Node<I, C>>, Integer> createBiddingNodeMap() {
        // TODO: filter these programatically by arity.
        Map<Supplier<Node<I, C>>, Integer> suppliers = new HashMap<>();
        ALL_BINARY_OPERATORS.stream()
                .forEach(op -> suppliers.put(() -> binary(op), 1));
        ALL_UNARY_OPERATORS.stream()
                .forEach(op -> suppliers.put(() -> unary(op), 1));
        ALL_AGGREGATORS.stream()
                      .forEach(op -> suppliers.put(() -> aggregator(op), 1));
        addNonTerminalBiddingNodeSuppliers(suppliers);
        addAggregatedBidNodeSuppliers(suppliers);
        nonTerminalSuppliers = new HashMap<>(suppliers);
        terminalSuppliers = terminalSuppliers();
        suppliers.putAll(terminalSuppliers);
        return suppliers;
    }

    private Map<Supplier<Node<I, C>>, Integer> terminalSuppliers() {
        Map<Supplier<Node<I, C>>, Integer> suppliers = new HashMap<>();
        suppliers.put(this::createEphemeralRandom, 1);
        suppliers.put(this::createEphemeralIntegerRandom, 1);
        suppliers.put(() -> new RandomNode(rng), 1);
        suppliers.put(() -> (Node<I, C>) new ItemNode(), 1);
        suppliers.put(() -> (Node<I, C>) new ItemNode(), 1);
        suppliers.put(() -> (Node<I, C>) new HandSize(), 1);
        suppliers.put(() -> (Node<I, C>) new BidsSoFar(), 1);
        suppliers.put(() -> (Node<I, C>) new RemainingBidNode(), 1);
        return suppliers;
    }

    private void addNonTerminalBiddingNodeSuppliers(Map<Supplier<Node<I, C>>, Integer> suppliers) {
        suppliers.put(() -> (Node<I, C>) new TrumpsInHand(), 1);
        suppliers.put(() -> (Node<I, C>) new NonTrumpsInHand(), 1);
    }

    private void addAggregatedBidNodeSuppliers(Map<Supplier<Node<I, C>>, Integer> suppliers) {
        ALL_AGGREGATORS.stream()
                       .forEach(ag -> suppliers.put(() -> (Node<I, C>) aggregatedRankData(ag), 1));
    }

    private static <I,C>  BinaryNode<I, C> binary(BinaryNode.Operator op) {
        return new BinaryNode<>(op);
    }

}
