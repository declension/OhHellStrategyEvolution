package net.declension.ea.cards.ohhell.nodes;

public interface NodeVisitor<I, C>  {
    void visit(Node<I, C> node);
}
