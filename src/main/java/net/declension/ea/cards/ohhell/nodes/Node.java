package net.declension.ea.cards.ohhell.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public abstract class Node<I, C> implements Evaluator<I, C> {
    protected List<Node<I, C>> children = new ArrayList<>();


    protected abstract Number doEvaluation(I item, C context);

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

    public List<Node<I, C>> children() {
        return children;
    }

    public Node<I, C> setChildren(Collection<? extends Node<I, C>> children) {
        this.children.clear();
        this.children.addAll(children);
        return this;
    }

    public Node<I, C> child(int i) {
        return children.get(i);
    }

    @Override
    public final Number evaluate(I item, C context) {
        checkChildren();
        return doEvaluation(item, context);
    }

    public abstract Optional<Integer> arity();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        return children.equals(node.children);
    }

    @Override
    public int hashCode() {
        return getClass().getSimpleName().hashCode() * 31 + children.hashCode();
    }
}
