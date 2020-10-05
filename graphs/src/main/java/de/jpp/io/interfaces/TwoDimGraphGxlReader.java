package de.jpp.io.interfaces;

import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class TwoDimGraphGxlReader extends GxlReaderTemplate<XYNode, Double, TwoDimGraph, String> {

    public TwoDimGraphGxlReader() {
    }

    @Override
    public TwoDimGraph read(String input) throws ParseException {

        Map<String, XYNode> map = new HashMap<>();
        TwoDimGraph resGraph = createGraph();

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

                double x = 0;
                double y = 0;
                boolean xEnthalten = false;
                boolean yEnthalten = false;
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

                //X & Y auslesen
                for (int j = 0; j < nodes.get(i).getChildren().size(); j++) {
                    if (nodes.get(i).getChildren().get(j).getAttributeValue("name").equals("x")) {
                        if (nodes.get(i).getChildren().get(j).getValue() == null) {
                            throw new ParseException("Attribut X darf nicht null sein");
                        }
                        try {
                            x = Double.parseDouble(nodes.get(i).getChildren().get(j).getValue());
                        } catch (NumberFormatException e) {
                            throw new ParseException("X muss Double sein");
                        }
                        xEnthalten = true;
                    } else if (nodes.get(i).getChildren().get(j).getAttributeValue("name").equals("y")) {
                        if (nodes.get(i).getChildren().get(j).getValue() == null) {
                            throw new ParseException("Attribut Y darf nicht null sein");
                        }
                        try {
                            y = Double.parseDouble(nodes.get(i).getChildren().get(j).getValue());
                        } catch (NumberFormatException e) {
                            throw new ParseException("Y muss Double sein");
                        }
                        yEnthalten = true;
                    }
                }
                if ((xEnthalten == false) || (yEnthalten == false)) {
                    throw new ParseException("Attribut X & Y müssen enthalten sein");
                }

                //Node erstellen
                XYNode xyNode = new XYNode(label, x, y);
                //Zum Graph hinzufügen
                map.put(id, xyNode);
                resGraph.addNode(xyNode);
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
                XYNode nodeFrom, nodeTo;
                try {
                    nodeFrom = map.get(from);
                    nodeTo = map.get(to);
                } catch (Exception e) {
                    throw new ParseException("nicht in Map enthalten");
                }
                //Edge zu Graph hinzufügen
                resGraph.addEuclidianEdge(nodeFrom, nodeTo);
            }
        } catch (IOException e) {
            throw new ParseException("IOException");
        } catch (JDOMException e) {
            throw new ParseException("JDOMException");
        }
        return resGraph;
    }

    public TwoDimGraph createGraph() {
        return new TwoDimGraph(new GraphImpl());
    }


    //Ab hier eigentlich unnötig (vollständigkeitshalber)
    public String readNodeId(XYNode node, Element element) {
        return element.getAttributeValue("id");
    }

    public XYNode readNode(Element element) {
        //id
        String id = element.getAttributeValue("id");

        double x = 0;
        double y = 0;
        boolean xEnthalten = false;
        boolean yEnthalten = false;
        String label = "";
        //Label (Description)
        for (int k = 0; k < element.getChildren().size(); k++) {
            if (element.getChildren().get(k).getAttributeValue("name").equals("description")) {
                if (element.getChildren().get(k).getChildren().size() == 0) {
                    try {
                        throw new ParseException("Label darf nicht null sein");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                label = element.getChildren().get(k).getValue().trim();
            }
        }

        //X & Y auslesen
        for (int j = 0; j < element.getChildren().size(); j++) {
            if (element.getChildren().get(j).getAttributeValue("name").equals("x")) {
                if (element.getChildren().get(j).getValue() == null) {
                    try {
                        throw new ParseException("Attribut X darf nicht null sein");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    x = Double.parseDouble(element.getChildren().get(j).getValue());
                } catch (NumberFormatException e) {
                    try {
                        throw new ParseException("X muss Double sein");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                xEnthalten = true;
            } else if (element.getChildren().get(j).getAttributeValue("name").equals("y")) {
                if (element.getChildren().get(j).getValue() == null) {
                    try {
                        throw new ParseException("Attribut Y darf nicht null sein");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    y = Double.parseDouble(element.getChildren().get(j).getValue());
                } catch (NumberFormatException e) {
                    try {
                        throw new ParseException("Y muss Double sein");
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                yEnthalten = true;
            }
        }
        if ((xEnthalten == false) || (yEnthalten == false)) {
            try {
                throw new ParseException("Attribut X & Y müssen enthalten sein");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Node erstellen
        XYNode xyNode = new XYNode(label, x, y);
        //map.put(id, xyNode);
        return xyNode;
    }

    public Optional<Double> readAnnotation(Element element) {
        String label = "";
        if (element.getAttributeValue("name").equals("description")) {
            if (element.getChildren().size() == 0) {
                try {
                    throw new ParseException("Label darf nicht null sein");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            label = element.getValue().trim();

        }
        return Optional.of(Double.parseDouble(label));
    }
}
