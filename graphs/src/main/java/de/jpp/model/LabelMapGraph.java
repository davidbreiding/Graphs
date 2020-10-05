package de.jpp.model;

import de.jpp.model.interfaces.Edge;
import de.jpp.model.interfaces.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A LabelMapGraph. <br>
 * The abstract-tag is only set because the tests will not compile otherwise. You should remove it!
 */
public class LabelMapGraph extends GraphImpl<String, Map<String, String>> implements Graph<String, Map<String, String>> {

    private List<String> nodes;
    private Map<String, List<Edge<String, String>>> edges;


    public LabelMapGraph() {
        this.nodes = new ArrayList<>();
        this.edges = new HashMap<>();
    }

    @Override
    public String toString() {
        return "LabelMapGraph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}

