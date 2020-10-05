package de.jpp.factory;

import de.jpp.model.GraphImpl;
import de.jpp.model.LabelMapGraph;
import de.jpp.model.ObservableGraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.interfaces.Graph;
import de.jpp.model.interfaces.ObservableGraph;

public class GraphFactory {

    /**
     * Creates a new and empty graph Instance which is not an ObservableGraph
     *
     * @param <N> the class type of nodes in this graph
     * @param <A> the annotation type of edges in this graph
     * @return a new and empty Graph instance
     */
    public <N, A> Graph<N, A> createNewGraph() {
        return new GraphImpl();
    }

    /**
     * Creates a new and empty graph Instance which is also an ObservableGraph
     *
     * @param <N> the class type of nodes in this graph
     * @param <A> the annotation type of edges in this graph
     * @return a new and empty Graph instance
     */
    public <N, A> ObservableGraph<N, A> createNewObservableGraph() {
        return new ObservableGraphImpl();
    }

    /**
     * Creates a new and empty TwoDimGraph instance
     *
     * @return a new and empty TwsoDimGraph instance
     */
    public TwoDimGraph createNewTwoDimGraph() {
        return new TwoDimGraph(new GraphImpl());
    }

    public LabelMapGraph createNewLabelMapGraph() {
        return new LabelMapGraph();
    }

}
