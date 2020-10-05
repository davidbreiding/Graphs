package de.jpp.algorithm.interfaces;

import de.jpp.factory.SearchStopFactory;
import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.Stack;

public class DepthFirstSearch<N, A, G extends Graph<N, A>> implements SearchAlgorithm<N, A, G> {

    boolean stopped;
    G graph;
    N start;
    SearchResultImpl<N, A> result;

    public DepthFirstSearch(G graph, N start) {
        this.graph = graph;
        this.start = start;
        this.stopped = false;
        result = new SearchResultImpl();
    }


    @Override
    public SearchResult<N, A> findPaths(SearchStopStrategy<N> type) {

        result.clear();

        Stack<N> nodeStack = new Stack<>();

        try {
            for (N node : graph.getNodes()) {
                result.statusMap.put(node, NodeStatus.UNKOWN);
            }

            nodeStack.push(start);

            result.setOpen(start);

            ///////
            result.setClosed(start);
            if (stopped) {
                return result;
            }
            result.infoMap.put(start, null);


            while (!nodeStack.empty()) {
                N node = nodeStack.pop();
                if (result.statusMap.get(node).equals(NodeStatus.OPEN) || node.equals(start)) {

                    if (graph.getNeighbours(node) != null) {
                        for (Edge<N, A> neighbours : graph.getNeighbours(node)) {
                            if (result.statusMap.get(neighbours.getDestination()).equals(NodeStatus.UNKOWN)) {
                                nodeStack.push(neighbours.getDestination());
                                if (!stopped) {
                                    result.setOpen(neighbours.getDestination());
                                }
                                result.infoMap.put(neighbours.getDestination(), new NodeInformation<>(new Edge(neighbours.getStart(), neighbours.getDestination(), neighbours.getAnnotation()), 0));
                            }
                        }
                    }
                    result.setClosed(node);

                    //Abbruchbedingung
                    if (type.stopSearch(node) || stopped) {
                        return result;
                    }
                }
            }
            return result;
        } catch (NullPointerException nullpointer) {
            return result;
        }
    }

    @Override
    public SearchResult<N, A> findAllPaths() {
        return findPaths(new SearchStopFactory().expandAllNodes());
    }

    @Override
    public ObservableSearchResult<N, A> getSearchResult() {
        return result;
    }

    @Override
    public N getStart() {
        return start;
    }

    @Override
    public G getGraph() {
        return graph;
    }

    @Override
    public void stop() {
        stopped = true;
    }


}
