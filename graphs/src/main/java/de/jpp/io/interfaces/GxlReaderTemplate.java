package de.jpp.io.interfaces;

import de.jpp.model.interfaces.Graph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public abstract class GxlReaderTemplate<N, A, G extends Graph<N, A>, F> implements GraphReader<N, A, G, F> {

    public GxlReaderTemplate() {

    }

    //TODO sinnfrei bei mir
    public G read(String input) throws ParseException {
        Map<String, N> map = new HashMap<>();
        G resGraph = createGraph();

        try {
            Document doc = new SAXBuilder().build(new StringReader(input));

            Element root = doc.getRootElement();
            Element graph = root.getChildren().get(0);

            List<Element> nodes = new ArrayList<>(graph.getChildren("node"));
            List<Element> edges = new ArrayList<>(graph.getChildren("edge"));

            if (!root.getName().equals("gxl")) {
                throw new ParseException("Root Element muss gxl sein");
            }

            //Nodes
            for (int i = 0; i < nodes.size(); i++) {

                //id
                String id = nodes.get(i).getAttributeValue("id");

                //Label (Description)
                String label = "";
                for (int k = 0; k < nodes.get(i).getChildren().size(); k++) {
                    if (nodes.get(i).getChildren().get(k).getAttributeValue("name").equals("description")) {
                        if (nodes.get(i).getChildren().get(k).getChildren().size() == 0) {
                            throw new ParseException("Label darf nicht null sein");
                        }
                        label = nodes.get(i).getChildren().get(k).getValue().trim();
                        System.out.println(label);
                    }

                    N node = readNode(nodes.get(i));
                    map.put(id, node);
                }
            }

            //Edges
            //erste Edge aus Liste edges ziehen
            for (int i = 0; i < edges.size(); i++) {
                if (edges.get(i).getAttributeValue("from") == null) {
                    throw new ParseException("From Node darf nicht null sein");
                }
                String from = edges.get(i).getAttributeValue("from");

                if (edges.get(i).getAttributeValue("to") == null) {
                    throw new ParseException("To Node darf nicht null sein");
                }
                String to = edges.get(i).getAttributeValue("to");
            }
        } catch (IOException e) {
            throw new ParseException("IOException");
        } catch (JDOMException e) {
            throw new ParseException("JDOMException");
        }
        return resGraph;
    }


    private void addEdge(Graph<N, A> graph, Element element, Map<String, N> map) {
    }

    public abstract G createGraph();

    public abstract String readNodeId(N node, Element element);

    public abstract N readNode(Element element);

    public abstract Optional<A> readAnnotation(Element element);


}
