package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.ObservableGraph;

import java.util.*;
import java.util.function.Consumer;

public class ObservableGraphImpl<N, A> implements ObservableGraph<N, A> {

    private GraphImpl graphImpl;

    private List<Consumer<N>> nodeAddedListener;
    private List<Consumer<N>> nodeRemovedListener;
    private List<Consumer<Edge<N, A>>> edgeAddedListener;
    private List<Consumer<Edge<N, A>>> edgeRemovedListener;

    private List<Consumer<Collection<Edge<N, A>>>> neighboursListedListener;
    private List<Consumer<Collection<Edge<N, A>>>> reachableListedListener;
    private List<Consumer<Collection<Edge<N, A>>>> edgesListedListener;
    private List<Consumer<Collection<N>>> nodesListedListener;

    public ObservableGraphImpl() {
        this.graphImpl = new GraphImpl();

        nodeAddedListener = new ArrayList<>();
        nodeRemovedListener = new ArrayList<>();
        edgeAddedListener = new ArrayList<>();
        edgeRemovedListener = new ArrayList<>();

        neighboursListedListener = new ArrayList<>();
        reachableListedListener = new ArrayList<>();
        edgesListedListener = new ArrayList<>();
        nodesListedListener = new ArrayList<>();
    }

    public ObservableGraphImpl(GraphImpl graphImpl) {
        this.graphImpl = graphImpl;

        nodeAddedListener = new ArrayList<>();
        nodeRemovedListener = new ArrayList<>();
        edgeAddedListener = new ArrayList<>();
        edgeRemovedListener = new ArrayList<>();

        neighboursListedListener = new ArrayList<>();
        reachableListedListener = new ArrayList<>();
        edgesListedListener = new ArrayList<>();
        nodesListedListener = new ArrayList<>();
    }

    //Müssen wegen implements ObservableGraph implementiert werden
    @Override
    public void addNodeAddedListener(Consumer<N> listener) {
        nodeAddedListener.add(listener);
    }

    @Override
    public void addNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListener.add(listener);
    }

    @Override
    public void addEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListener.add(listener);
    }

    @Override
    public void addEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListener.add(listener);
    }

    @Override
    public void removeNodeAddedListener(Consumer<N> listener) {
        nodeAddedListener.remove(listener);
    }

    @Override
    public void removeNodeRemovedListener(Consumer<N> listener) {
        nodeRemovedListener.remove(listener);
    }

    @Override
    public void removeEdgeAddedListener(Consumer<Edge<N, A>> listener) {
        edgeAddedListener.remove(listener);
    }

    @Override
    public void removeEdgeRemovedListener(Consumer<Edge<N, A>> listener) {
        edgeRemovedListener.remove(listener);
    }

    @Override
    public void addNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListener.add(listener);
    }

    @Override
    public void addReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListener.add(listener);
    }

    @Override
    public void addNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListener.add(listener);
    }

    @Override
    public void addEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListener.add(listener);
    }

    @Override
    public void removeNeighboursListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        neighboursListedListener.remove(listener);
    }

    @Override
    public void removeReachableListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        reachableListedListener.remove(listener);
    }

    @Override
    public void removeNodesListedListener(Consumer<Collection<N>> listener) {
        nodesListedListener.remove(listener);
    }

    @Override
    public void removeEdgesListedListener(Consumer<Collection<Edge<N, A>>> listener) {
        edgesListedListener.remove(listener);
    }

    @Override
    public boolean addNode(N node) {

        for (Consumer<N> nConsumer : nodeAddedListener) {
            nConsumer.accept(node);
        }
        return graphImpl.addNode(node);
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes) {

        boolean change = false;
        boolean change1 = false;

        Iterator i = nodes.iterator();

        while (i.hasNext()) {
            N node = (N) i.next();
            change = addNode(node);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    @Override
    public boolean addNodes(N... nodes) {
        boolean change = false;
        boolean change1 = false;

        for (int i = 0; i < nodes.length; i++) {
            change = addNode(nodes[i]);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    @Override
    public Collection<N> getNodes() {

        for (Consumer<Collection<N>> collectionConsumer : nodesListedListener) {
            collectionConsumer.accept(graphImpl.getNodes());
        }
        return graphImpl.getNodes();
    }


    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {

        if (!graphImpl.getNodes().contains(start)) {
            addNode(start);
        }
        if (!graphImpl.getNodes().contains(destination)) {
            addNode(destination);
        }

        for (Consumer<Edge<N, A>> edgeConsumer : edgeAddedListener) {
            edgeConsumer.accept(new Edge<N, A>(start, destination, annotation));
        }
        return graphImpl.addEdge(start, destination, annotation);
    }

    @Override
    public boolean removeEdge(Edge<N, A> edge) {

        for (Consumer<Edge<N, A>> edgeConsumer : edgeRemovedListener) {
            edgeConsumer.accept(edge);
        }
        return graphImpl.removeEdge(edge);
    }

    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {

        for (Consumer<Collection<Edge<N, A>>> collectionConsumer : neighboursListedListener) {
            collectionConsumer.accept(graphImpl.getNeighbours(node));
        }
        return graphImpl.getNeighbours(node);
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {

        for (Consumer<Collection<Edge<N, A>>> collectionConsumer : reachableListedListener) {
            collectionConsumer.accept(graphImpl.getReachableFrom(node));
        }

        return graphImpl.getReachableFrom(node);
    }

    @Override
    public Collection<Edge<N, A>> getEdges() {

        for (Consumer<Collection<Edge<N, A>>> collectionConsumer : edgesListedListener) {
            collectionConsumer.accept(graphImpl.getEdges());
        }
        return graphImpl.getEdges();
    }

    @Override
    public boolean removeNode(N node) {

        Iterator i = graphImpl.getEdges().iterator();
        while (i.hasNext()) {
            Edge<N, A> e = (Edge<N, A>) i.next();
            if ((node == e.getStart()) || node == e.getDestination()) {
                for (Consumer<Edge<N, A>> edgeConsumer : edgeRemovedListener) {
                    edgeConsumer.accept(e);
                }
            }
        }

        for (Consumer<N> nConsumer : nodeRemovedListener) {
            nConsumer.accept(node);
        }
        return graphImpl.removeNode(node);
    }

    @Override
    public boolean removeNodes(Collection<? extends N> nodes) {
        boolean change = false;
        boolean change1 = false;

        Iterator i = nodes.iterator();

        while (i.hasNext()) {
            N node = (N) i.next();
            change = removeNode(node);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    @Override
    public boolean removeNodes(N... nodes) {
        boolean change = false;
        boolean change1 = false;
        for (int i = 0; i < nodes.length; i++) {
            change = removeNode(nodes[i]);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    @Override
    public void clear() {
//TODO muss kann ich innerhalb der for schleife da mehrmals die i.next aufrufen? Oder springt das dann immer ungewollt weiter
        Iterator<Edge<N, A>> i = graphImpl.getEdges().iterator();
        while (i.hasNext()) {
            for (Consumer<Edge<N, A>> edgeConsumer : edgeRemovedListener) {
                edgeConsumer.accept(i.next());
            }
        }

        Iterator<N> itr = graphImpl.getNodes().iterator();
        while (itr.hasNext()) {
            for (Consumer<N> nConsumer : nodeRemovedListener) {
                nConsumer.accept(itr.next());
            }
        }
        graphImpl.clear();
    }

    @Override
    public String toString() {
        return "ObservableGraphImpl{" +
                "graphImpl=" + graphImpl + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o.getClass() != getClass()) return false;
        ObservableGraphImpl<N, A> graph = (ObservableGraphImpl<N, A>) o;
        if (graphImpl.equals(graph.graphImpl)) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}



