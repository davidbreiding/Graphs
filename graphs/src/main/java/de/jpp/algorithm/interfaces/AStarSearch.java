package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.LinkedList;

public class AStarSearch<N, A, G extends Graph<N, A>> extends BreadthFirstSearchTemplate<N, A, G> {

    N destination;
    EstimationFunction estToDest;

    public AStarSearch(G graph, N start, N dest, EstimationFunction<N> estToDest) {
        super(graph, start);
        this.destination = dest;
        this.estToDest = estToDest;
    }

    @Override
    double calcDist(Edge<N, A> edge, N node, SearchResultImpl<N, A> result) {

        if (!result.infoMap.containsKey(node)) {
            return Double.parseDouble(String.valueOf(edge.getAnnotation().get()));
        }
        double distEdge = Double.parseDouble(String.valueOf(edge.getAnnotation().get()));
        double distStartNode = result.infoMap.get(node).distance;
        double newDistDest = distEdge + distStartNode;

        //Vielleicht ist Zielknoten ja noch gar nicht drin
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
        double h = Double.MAX_VALUE;
        for (N n : queue) {

            if (estToDest.getEstimatedDistance(n, destination) < h) {
                h = estToDest.getEstimatedDistance(n, destination);
                resNode = n;
            }
        }
        return resNode;
    }
}
