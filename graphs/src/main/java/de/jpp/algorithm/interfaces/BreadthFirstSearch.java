package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.LinkedList;

public class BreadthFirstSearch<N, A, G extends Graph<N, A>> extends BreadthFirstSearchTemplate<N, A, G> {


    public BreadthFirstSearch(G graph, N start) {
        super(graph, start);
    }

    @Override
    double calcDist(Edge<N, A> edge, N node, SearchResultImpl<N, A> result) {
        return 1;
    }

    @Override
    NodeInformation<N, A> getNodeInformation(Edge<N, A> edge, double distance) {
        return new NodeInformation<>(edge, distance);
    }

    @Override
    N popNode(LinkedList<N> queue, SearchResultImpl<N, A> result) {
        return queue.peek();
    }
}
