package de.jpp.algorithm.interfaces;

import de.jpp.factory.SearchStopFactory;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.LinkedList;

public abstract class BreadthFirstSearchTemplate<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {

    private boolean stopped;
    private G graph;
    private N start;
    private SearchResultImpl<N, A> result;

    public BreadthFirstSearchTemplate(G graph, N start) {
        this.stopped = false;
        this.graph = graph;
        this.start = start;
        this.result = new SearchResultImpl<>();
    }

    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {

        //alle Nodes in statusMap und UNKNOWN
        for (N node : graph.getNodes()) {
            result.getStatusMap().put(node, NodeStatus.UNKOWN);
        }

        LinkedList<N> queue = new LinkedList<N>();


        //Startknoten öffnen und in queue & infoMap einfügen
        queue.add(start);
        result.setOpen(start);
        result.setClosed(start);
        if (stopped) {
            return result;
        }


        while (queue.size() != 0) {
            N node = popNode(queue, result);
            queue.remove(node);

            //Nachbarn durchgehen, auf open setzen und queue hinzufügen
            try {
                for (Edge<N, A> neighbour : graph.getNeighbours(node)) {
                    if (!result.getStatusMap().get(neighbour.getDestination()).equals(NodeStatus.CLOSED)) {

                        queue.add(neighbour.getDestination());

                        if (!result.infoMap.containsKey(neighbour.getDestination()) || result.getInformation(neighbour.getDestination()).distance > calcDist(neighbour, node, result)) {
                            result.infoMap.put(neighbour.getDestination(), new NodeInformation<>(neighbour, calcDist(neighbour, node, result)));
                            result.setOpen(neighbour.getDestination());
                        }
                    }
                }
            } catch (NullPointerException nl) {

            }
            if (!node.equals(start)) {
                result.setClosed(node);
            }
            if (type.stopSearch(node) || stopped) {
                return result;
            }
        }
        return result;
    }

    public SearchResult<N, A> findAllPaths() {
        return findPaths(new SearchStopFactory().expandAllNodes());
    }

    public ObservableSearchResult<N, A> getSearchResult() {
        return result;
    }

    public N getStart() {
        return start;
    }

    public G getGraph() {
        return graph;
    }

    public void stop() {
        stopped = true;
    }

    abstract double calcDist(Edge<N, A> edge, N node, SearchResultImpl<N, A> result);

    abstract NodeInformation getNodeInformation(Edge<N, A> edge, double distance);

    abstract N popNode(LinkedList<N> queue, SearchResultImpl<N, A> result);

    private void openIfShorter(N node, NodeInformation nodeInformation) {

    }
}
