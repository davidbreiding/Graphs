package de.jpp.model.interfaces;


import de.jpp.io.interfaces.ParseException;

import java.util.Objects;
import java.util.Optional;

public class Edge<N, A> {


    /**
     * Creates a new edge with the specified start node, destination node and annotation
     *
     * @param start      the start node
     * @param dest       the destination node
     * @param annotation the annotation
     */

    private N start;
    private N dest;
    private Optional<A> annotation;


    public Edge(N start, N dest, Optional<A> annotation) {

        //TODO hier war ArgumentException
        if (start == null || dest == null) {
            try {
                throw new ParseException("start, dest & annotation dürfen nicht null sein");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        this.start = start;
        this.dest = dest;

        if (annotation == null) {
            this.annotation = Optional.empty();
        } else {
            this.annotation = annotation;
        }
    }


    /**
     * Returns the start node of this edge
     *
     * @return the start node of this edge
     */
    public N getStart() {
        return start;
    }

    /**
     * Returns the destination node of this edge
     *
     * @return the destination node of this edge
     */
    public N getDestination() {
        return dest;
    }

    /**
     * Returns the annotation of this edge
     *
     * @return the annotation of this edge
     */
    public Optional<A> getAnnotation() {
        return annotation;
    }


    @Override
    public String toString() {
        return "Edge{" +
                "start=" + start +
                ", dest=" + dest +
                ", annotation=" + annotation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?, ?> edge = (Edge<?, ?>) o;
        return Objects.equals(start, edge.start) &&
                Objects.equals(dest, edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, dest);
    }
}
