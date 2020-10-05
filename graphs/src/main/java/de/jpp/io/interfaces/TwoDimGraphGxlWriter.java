package de.jpp.io.interfaces;

import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.HashMap;
import java.util.Map;


public class TwoDimGraphGxlWriter extends GxlWriterTemplate<XYNode, Double, TwoDimGraph, String> {
    @Override
    public String write(TwoDimGraph graph) {
        Map<String, Integer> map = new HashMap<>();

        Document doc = new Document();
        Element root = new Element("gxl");

        Element graphTag = new Element("graph");
        root.addContent(graphTag);

        //Nodes
        int count = 1;
        for (XYNode node : graph.getNodes()) {
            Element nodeAktuell = new Element("node");
            String counter = String.valueOf(count);
            nodeAktuell.setAttribute("id", counter);
            graphTag.addContent(nodeAktuell);

            Element xTag = new Element("attr");
            xTag.setAttribute("name", "x");
            nodeAktuell.addContent(xTag);

            Element xZahl = new Element("float");
            String x = String.valueOf(node.getX());
            xZahl.setText(x);
            xTag.addContent(xZahl);

            Element yTag = new Element("attr");
            yTag.setAttribute("name", "y");
            nodeAktuell.addContent(yTag);

            Element yZahl = new Element("float");
            String y = String.valueOf(node.getY());
            yZahl.setText(y);
            yTag.addContent(yZahl);

            Element desc = new Element("attr");
            desc.setAttribute("name", "description");
            nodeAktuell.addContent(desc);

            Element descCont = new Element("string");
            descCont.setText(node.getLabel());
            desc.addContent(descCont);

            map.put(node.getLabel(), count);

            count++;
        }


        //Edges

        for (Edge<XYNode, Double> edge : graph.getEdges()) {
            Element edgeAktuell = new Element("edge");
            String counter = String.valueOf(count);
            edgeAktuell.setAttribute("from", String.valueOf(map.get(edge.getStart().getLabel())));
            edgeAktuell.setAttribute("id", counter);
            edgeAktuell.setAttribute("to", String.valueOf(map.get(edge.getDestination().getLabel())));
            graphTag.addContent(edgeAktuell);

            Element attr = new Element("attr");
            attr.setAttribute("name", "description");
            edgeAktuell.addContent(attr);

            Element stringTag = new Element("string");
            stringTag.setText(counter);
            attr.addContent(stringTag);

            count++;
        }


        doc.setContent(root);


        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String xmlString = outputter.outputString(doc);


        return xmlString;
    }
}
