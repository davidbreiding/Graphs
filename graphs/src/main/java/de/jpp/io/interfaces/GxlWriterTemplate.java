package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;
import org.jdom2.Element;

public abstract class GxlWriterTemplate<N, A, G extends Graph<N, A>, F> implements GraphWriter<N, A, G, F> {
    public GxlWriterTemplate() {

    }

    public String write(G graph) {
        return null;
    }

    public Element writeNode(N node) {
        return null;
    }

    public Element writeEdge(Edge<N, A> edge) {
        return null;
    }

    public String calculateId(N node) {
        return null;
    }

    public String calculateId(Edge<N, A> edge) {
        return null;
    }
}
