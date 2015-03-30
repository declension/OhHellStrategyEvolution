package net.declension.ea.cards.ohhell.nodes;

import java.util.*;

import static java.lang.String.format;

public abstract class Node<I, C> implements Evaluator<I, C> {
    protected List<Node<I, C>> children = new ArrayList<>();


    protected abstract Number doEvaluation(I item, C context);

    public Node<I, C> child(int i) {
        return children.get(i);
    }

    public List<Node<I, C>> children() {
        return children;
    }

    public Node<I, C> setChildren(Collection<? extends Node<I, C>> children) {
        this.children.clear();
        this.children.addAll(children);
        return this;
    }

    /**
     * Returns a copy of the current Node mutated, in the way that sub-classes choose to implement.
     * Typically this will be change the operator or value, but does <strong>not</strong> mean that children will
     * be affected.
     *
     * Sub-classes should implement this to enable mutation, as it default to a no-op.
     * @param <T> The sub-type, to allow returning stricter-typed subclasses of {@link Node}
     * @param rng The source of randomness to use
     */
    public <T extends Node<I, C>> T mutatedCopy(Random rng) {
        return (T) this;
    }

    protected void checkChildren() {
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

    @Override
    public final Number evaluate(I item, C context) {
        checkChildren();
        return doEvaluation(item, context);
    }

    public abstract Optional<Integer> arity();

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
