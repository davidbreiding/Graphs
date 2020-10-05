package de.jpp.algorithm.interfaces;

import de.jpp.model.interfaces.Edge;

import java.util.Objects;

public class NodeInformation<N, A> {
    Edge<N, A> predecessor;
    double distance;

    public NodeInformation(Edge<N, A> predecessor, double distance) {
        this.predecessor = predecessor;
        this.distance = distance;
    }


    public Edge<N, A> getPredecessor() {
        return predecessor;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeInformation<?, ?> that = (NodeInformation<?, ?>) o;
        return Double.compare(that.distance, distance) == 0 &&
                Objects.equals(predecessor, that.predecessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(predecessor, distance);
    }

    @Override
    public String toString() {
        return "NodeInformation{" +
                "predecessor=" + predecessor +
                ", distance=" + distance +
                '}';
    }
}



