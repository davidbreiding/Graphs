package de.jpp.io.interfaces;

import de.jpp.model.LabelMapGraph;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class LabelMapGraphGxlReader extends GxlReaderTemplate<String, Map<String, String>, LabelMapGraph, String> implements GraphReader<String, Map<String, String>, LabelMapGraph, String> {

    @Override
    public LabelMapGraph read(String input) throws ParseException {

        Map<String, String> map = new HashMap<>();
        LabelMapGraph resGraph = createGraph();

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

                String label = "";
                //Label (Description)
                for (int k = 0; k < nodes.get(i).getChildren().size(); k++) {
                    if (nodes.get(i).getChildren().get(k).getAttributeValue("name").equals("description")) {
                        if (nodes.get(i).getChildren().get(k).getChildren().size() == 0) {
                            throw new ParseException("Label darf nicht null sein");
                        }
                        label = nodes.get(i).getChildren().get(k).getValue().trim();
                    }
                }

                //Node erstellen
                resGraph.addNode(label);
                map.put(id, label);
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

                //Richtige Nodes aus Map ziehen
                String nodeFrom, nodeTo;
                try {
                    nodeFrom = map.get(from);
                    nodeTo = map.get(to);
                } catch (NullPointerException nl) {
                    throw new ParseException("Falscher Node in Edge");
                } catch (NumberFormatException pe) {
                    throw new ParseException("Falscher Node in Edge");
                }

                if (nodeFrom == null || nodeTo == null) {
                    throw new ParseException("Knoten nicht vorhanden");
                }

                //Edge zu Graph hinzufügen


                Map<String, String> keyValueMap = new HashMap<>();

                for (int j = 0; j < edges.get(i).getChildren().size(); j++) {
                    String attrName = edges.get(i).getChildren().get(j).getAttributeValue("name");
                    String inhalt = edges.get(i).getChildren().get(j).getValue();
                    attrName = attrName.trim();
                    inhalt = inhalt.trim();
                    keyValueMap.put(attrName, inhalt);
                }

                if (keyValueMap.size() == 0) {
                    resGraph.addEdge(nodeFrom, nodeTo);
                } else {
                    resGraph.addEdge(nodeFrom, nodeTo, Optional.of(keyValueMap));
                }
            }
        } catch (IOException e) {
            throw new ParseException("IOException");
        } catch (JDOMException e) {
            throw new ParseException("JDOMException");
        } catch (ParseException pe) {
            throw new ParseException("ParseException");
        }
        return resGraph;
    }


    @Override
    public LabelMapGraph createGraph() {
        return new LabelMapGraph();
    }

    @Override
    public String readNodeId(String node, Element element) {
        return null;
    }

    @Override
    public String readNode(Element element) {
        return null;
    }

    @Override
    public Optional<Map<String, String>> readAnnotation(Element element) {
        return Optional.empty();
    }
}
