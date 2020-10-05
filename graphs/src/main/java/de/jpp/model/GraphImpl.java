package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.*;
import java.util.stream.Stream;


public class GraphImpl<N, A> implements Graph<N, A> {

    private List<N> nodes;
    private Map<N, List<Edge<N, A>>> edges;

    public GraphImpl() {
        this.nodes = new ArrayList<N>();
        this.edges = new HashMap<>();
    }

    public GraphImpl(List<N> nodes, Map<N, List<Edge<N, A>>> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }


    //Methoden aus Diagramm
    private void removeHook(N node) {
        for (N key : edges.keySet()) {
            for (int j = 0; j < edges.get(key).size(); j++) {
                if (edges.get(key).get(j).getStart() == node || edges.get(key).get(j).getDestination() == node) {
                    removeEdge(edges.get(key).get(j));
                }
            }
        }
    }

    private void ensureEdgeListNonNull(N node) {
        if (edges.get(node) == null) {
            List<Edge<N, A>> list = new ArrayList<>();
            edges.put(node, list);
        }
    }

    //TODO nochmal checken
    private boolean addNodes(Stream<N> stream) {
        boolean change = false;
        boolean change1 = false;

        //N[] nodes = stream.toArray(N[]::new);

        for (int i = 0; i < stream.toArray().length; i++) {
            change = addNode((N) stream.toArray()[i]);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    private boolean removeNodes(Stream<N> stream) {
        boolean change = false;
        boolean change1 = false;

        //N[] nodes = stream.toArray(N[]::new);


        for (int i = 0; i < stream.toArray().length; i++) {
            change = removeNode((N) stream.toArray()[i]);
            if (change) {
                change1 = true;
            }
        }
        return change1;

    }
//bis hier


    //Getter & Setter

    public void setNodes(List<N> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(Map<N, List<Edge<N, A>>> edges) {
        this.edges = edges;
    }


    //Die hier wegen implements Graph
    @Override
    public boolean addNode(N node) {
        if (this.nodes.contains(node)) {
            return false;
        } else {
            this.nodes.add(node);
        }
        return true;
    }

    @Override
    public boolean addNodes(Collection<? extends N> nodes) {
        boolean change = false;
        boolean change1 = false;

        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            N n = (N) i.next();
            change = addNode(n);
            if (change) {
                change1 = true;
            }
        }
        return change1;
    }

    @Override
    public boolean addNodes(N... nodes) {
        boolean cont = false;
        boolean cont1 = false;

        for (int i = 0; i < nodes.length; i++) {
            cont = addNode(nodes[i]);
            if (cont) {
                cont1 = true;
            }
        }
        return cont1;
    }

    @Override
    public Collection<N> getNodes() {
        Collection<N> temp = new ArrayList<>(nodes);
        return temp;
    }

    @Override
    public Edge<N, A> addEdge(N start, N destination, Optional<A> annotation) {

        //Startknoten in Nodesliste enthalten?
        if (!nodes.contains(start)) {
            nodes.add(start);
        }

        //Zielknoten in Nodesliste enthalten?
        if (!nodes.contains(destination)) {
            nodes.add(destination);
        }

        //Ist Startknoten in Map als Key enthalten?
        //Wenn nicht, wird Knoten (Key) und leere Liste (Value) hinzugefügt
        if (!edges.containsKey(start)) {
            List<Edge<N, A>> l = new ArrayList<>();
            edges.put(start, l);
        }

        Edge<N, A> e = new Edge<>(start, destination, annotation);

        //Ist Edge in der Map in Values enthalten?
        //Falls ja, gebe diese Edge zurück
        if (edges.get(start).contains(e)) {
            return e;
        }

        //Falls nicht enthalten, neue Edge hinzufügen
        edges.get(start).add(e);

        return e;
    }

    public Edge<N, A> addEdge(N start, N destination) {
        return addEdge(start, destination, Optional.empty());
    }


    //todo in beide Richtungen oder nur in eine?
    //todo also kann startknoten bei einem zielknoten auch startknoten sein?
    @Override
    public boolean removeEdge(Edge<N, A> edge) {

        if (edge == null) {
            return false;
        }
        N start = edge.getStart();
        N dest = edge.getDestination();

        if (edges.containsKey(start)) {
            for (int i = 0; i < edges.get(start).size(); i++) {
                if (edges.get(start).get(i).getDestination() == dest) {
                    edges.get(start).remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Collection<Edge<N, A>> getNeighbours(N node) {
        return edges.get(node);
    }

    @Override
    public Collection<Edge<N, A>> getReachableFrom(N node) {
        Collection<Edge<N, A>> dests = new ArrayList<>();

        //== mit equals ersätzt
        for (N key : edges.keySet()) {
            for (int j = 0; j < edges.get(key).size(); j++) {
                if (edges.get(key).get(j).getDestination().equals(node)) {
                    dests.add(edges.get(key).get(j));
                }
            }
        }
        return dests;
    }

    @Override
    public Collection<Edge<N, A>> getEdges() {
        Collection<Edge<N, A>> edg = new ArrayList<>();

        for (N key : edges.keySet()) {
            for (int j = 0; j < edges.get(key).size(); j++) {
                edg.add(edges.get(key).get(j));
            }
        }
        return edg;
    }

    @Override
    public boolean removeNode(N node) {

        boolean change = false;

        if (edges.containsKey(node)) {
            edges.remove(node);
            change = true;
        }

        if (nodes.contains(node)) {
            nodes.remove(node);
            change = true;
        }


        for (N key : edges.keySet()) {
            for (int j = 0; j < edges.get(key).size(); j++) {
                if ((edges.get(key).get(j).getDestination().equals(node)) || (edges.get(key).get(j).getStart().equals(node))) {
                    edges.get(key).remove(j);
                    change = true;
                }
            }
        }
        return change;
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
        edges.clear();
        nodes.clear();
    }

    //TODO die beiden hier noch irgendwie richtig schreiben


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        GraphImpl<N, A> graph = (GraphImpl) o;

        //Gleiche Anzahl von Knoten und Kanten?
        if (this.edges.size() != graph.edges.size()) {
            return false;
        }
        if (this.nodes.size() != graph.nodes.size()) {
            return false;
        }

        //Gleiche Nodes/Edges jeweils enthalten?
        if (!this.getNodes().containsAll(graph.getNodes())) {
            return false;
        }

        //edges
        if (!this.getEdges().containsAll(graph.getEdges())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }


    @Override
    public String toString() {
        return "GraphImpl{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
