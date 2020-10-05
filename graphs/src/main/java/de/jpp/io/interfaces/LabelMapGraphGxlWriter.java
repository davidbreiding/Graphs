package de.jpp.io.interfaces;

import de.jpp.model.LabelMapGraph;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LabelMapGraphGxlWriter extends GxlWriterTemplate<String, Map<String, String>, LabelMapGraph, String> implements GraphWriter<String, Map<String, String>, LabelMapGraph, String> {

    public String write(LabelMapGraph graph) {

        Map<String, Integer> map = new HashMap<>();

        Document doc = new Document();
        Element root = new Element("gxl");

        Element graphTag = new Element("graph");
        root.addContent(graphTag);

        //Nodes
        int count = 1;
        for (String node : graph.getNodes()) {
            Element nodeAktuell = new Element("node");
            String counter = String.valueOf(count);
            nodeAktuell.setAttribute("id", counter);
            graphTag.addContent(nodeAktuell);
            map.put(node, count);

            Element attr = new Element("attr");
            attr.setAttribute("name", "description");
            nodeAktuell.addContent(attr);

            Element string = new Element("string");
            string.setText(node);
            attr.addContent(string);
            count++;
        }


        //Edges

        for (Edge<String, Map<String, String>> edge : graph.getEdges()) {
            Element edgeAktuell = new Element("edge");
            String counter = String.valueOf(count);
            edgeAktuell.setAttribute("from", String.valueOf(map.get(edge.getStart())));
            edgeAktuell.setAttribute("id", counter);
            edgeAktuell.setAttribute("to", String.valueOf(map.get(edge.getDestination())));
            graphTag.addContent(edgeAktuell);

            if (edge.getAnnotation().isPresent()) {
                Map<String, String> tempMap = edge.getAnnotation().get();
                Iterator<String> i = tempMap.keySet().iterator();
                while (i.hasNext()) {
                    String next = i.next();
                    Element attr = new Element("attr");
                    attr.setAttribute("name", next);
                    edgeAktuell.addContent(attr);

                    Element string = new Element("string");
                    string.setText(tempMap.get(next));
                    attr.addContent(string);
                }
            }
            count++;
        }


        doc.setContent(root);


        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(doc);


        return xmlString;
    }


}
