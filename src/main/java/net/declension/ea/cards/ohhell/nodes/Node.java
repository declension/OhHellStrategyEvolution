package net.declension.ea.cards.ohhell.nodes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static net.declension.utils.Validation.requireNonNullParam;

public abstract class Node<I, C> implements Evaluator<I, C>, Consumer<NodeVisitor<I, C>> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected List<Node<I, C>> children = new ArrayList<>();
    protected transient Optional<Node<I, C>> parent = Optional.empty();


    protected abstract Number doEvaluation(I item, C context);

    public abstract Optional<Integer> arity();

    public abstract Node<I,C> shallowCopy();

    public Node<I, C> child(int i) {
        return children.get(i);
    }

    public List<Node<I, C>> children() {
        return children;
    }

    public void addChild(Node<I, C> node) {
        children.add(node);
        node.parent = Optional.of(this);
    }

    public Node<I, C> setChildren(Collection<? extends Node<I, C>> children) {
        this.children.clear();
        this.children.addAll(children);
        children.forEach(child -> child.parent = Optional.of(this));
        return this;
    }

    /**
     * Replaces this node with another, fixing the tree above and below.
     * @param replacement the new node.
     */
    public void replaceWith(Node<I, C> replacement) {
        requireNonNullParam(replacement, "Replacement node");
        if (!parent.isPresent()) {
            return;
        }
        children.forEach(child -> child.parent = Optional.of(replacement));
        //replacement.setChildren(children);
        children.clear();
        parent.get().replaceChild(this, replacement);
    }

    public void replaceChild(Node<I, C> child, Node<I, C> replacement) {
        int index = children.indexOf(child);
        if (index == -1) {
            throw new IllegalArgumentException(format("%s doesn't have a child %s to replace.", this, child));
        }
        child.parent = Optional.empty();
        replacement.parent = Optional.of(this);
        children.set(index, replacement);
    }


    /**
     * Returns a copy of the current Node mutated, in the way that sub-classes choose to implement.
     * Typically this will be change the operator or value, but does <strong>not</strong> mean that children will
     * be affected.
     *
     * @param rng The source of randomness to use
     */
    public Node<I,C> mutate(Random rng) {
        //logger.debug("Mutation not possible on this type");
        return this;
    }

    /**
     * Checks that children are of the correct arity, and so on.
     */
    private void checkChildren() {
        arity().ifPresent(a -> {
            int numChildren = children.size();
            if (numChildren != a) {
                throw new IllegalStateException(
                        format("Can't evaluate %s(%d) with %d child%s!",
                               getClass().getSimpleName(), a, numChildren, numChildren == 1 ? "" : "ren"));
            }
        });
    }

    /**
     * A flat list of the tree below this
     * @return
     */
    public List<Node<I, C>> allDescendants() {
        List<Node<I, C>> allDescendants = new ArrayList<>();
        allDescendants.add(this);
        if (children.isEmpty()) {
            return allDescendants;
        }
        for (Node<I, C> child: children) {
            allDescendants.addAll(child.allDescendants());
        }
        return allDescendants;
    }

    /**
     * @return a "deep" copy with the whole tree below copied.
     */
    public Node<I, C> deepCopy() {
        Node<I, C> newRoot = shallowCopy();
        if (children.size() > 0) {
            List<Node<I, C>> newChildren = children.stream().map(c -> c.deepCopy()).collect(toList());
            newRoot.setChildren(newChildren);
        }
        return newRoot;
    }

    /**
     * Allows visitors to be invoked for every node in the tree from here and below.
     *
     * @param visitor the {@link NodeVisitor} on which to call
     * {@link net.declension.ea.cards.ohhell.nodes.NodeVisitor#visit(Node)}.
     */
    @Override
    public void accept(NodeVisitor<I, C> visitor) {
        visitor.visit(this);
        children.forEach(child -> child.accept(visitor));
    }

    @Override
    public final Number evaluate(I item, C context) {
        checkChildren();
        return doEvaluation(item, context);
    }

    /**
     * Base nodes are judged equal if their children are equal.
     * Any implementations adding state should make sure they compare this as well,
     * but can call this {@code super} implementation to do the boring checking.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Node node = (Node) other;

        return children.equals(node.children);
    }

    @Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode() * 31 + children.hashCode();
    }
}
