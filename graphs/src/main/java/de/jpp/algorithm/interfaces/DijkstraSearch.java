package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.LinkedList;

public class DijkstraSearch<N, A, G extends Graph<N, A>> extends BreadthFirstSearchTemplate<N, A, G> {

    public DijkstraSearch(G graph, N start) {
        super(graph, start);
    }

    @Override
    double calcDist(Edge<N, A> edge, N node, SearchResultImpl<N, A> result) {
        if (!result.infoMap.containsKey(node)) {
            return Double.parseDouble(String.valueOf(edge.getAnnotation().get()));
        }
        double distEdge = Double.parseDouble(String.valueOf(edge.getAnnotation().get()));
        double distStartNode = result.infoMap.get(node).distance;
        double newDistDest = distEdge + distStartNode;


        try {
            double distDestNode = result.infoMap.get(edge.getDestination()).distance;

            //Ist neue Entfernung kürzer als alte?

            if (newDistDest < distDestNode) {

                return newDistDest;
            }
            return distDestNode;

        } catch (NullPointerException nl) {
            return newDistDest;
        }
    }

    @Override
    NodeInformation getNodeInformation(Edge<N, A> edge, double distance) {
        return new NodeInformation(edge, distance);
    }

    @Override
    N popNode(LinkedList<N> queue, SearchResultImpl<N, A> result) {
        N resNode = null;
        double lowDist = Double.MAX_VALUE;
        for (N n : queue) {
            try {
                if (result.getInformation(n).distance < lowDist) {
                    lowDist = result.getInformation(n).distance;
                    resNode = n;
                }
            } catch (NullPointerException nl) {
                return n;
            }
        }
        return resNode;
    }
}
