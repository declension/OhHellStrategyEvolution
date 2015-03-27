package net.declension.ea.cards.ohhell.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public abstract class Node<I, C> implements Evaluator<I, C> {
    protected List<Node<I, C>> children = new ArrayList<>();

    @Override
    public final Number evaluate(I item, C context) {
        checkChildren();
        return doEvaluation(item, context);
    }

    protected abstract Number doEvaluation(I item, C context);

    protected void checkChildren() {
        arity().ifPresent(a -> {
            int numChildren = children.size();
            if (numChildren != a) {
                throw new IllegalStateException(
                        format("Can't evaluate %s(%d) with %d child%s!",
                               getClass().getSimpleName(), a, numChildren, numChildren == 1? "" : "ren"));
            }
        });
    }

    public List<Node<I, C>> children() {
        return children;
    }

    public Node<I, C> setChildren(List<? extends Node<I, C>> children) {
        this.children = new ArrayList<>(children);
        return this;
    }

    public abstract Optional<Integer> arity();
}
