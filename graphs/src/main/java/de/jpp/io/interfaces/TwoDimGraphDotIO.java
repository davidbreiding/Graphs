package de.jpp.io.interfaces;

import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;
import de.jpp.model.interfaces.Edge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TwoDimGraphDotIO implements GraphReader<XYNode, Double, TwoDimGraph, String>, GraphWriter<XYNode, Double, TwoDimGraph, String> {

    @Override
    public TwoDimGraph read(String input) throws ParseException {

        TwoDimGraph graph = new TwoDimGraph(new GraphImpl());
        try {
            Reader eingabeString = new StringReader(input);
            BufferedReader bufferedReader = new BufferedReader(eingabeString);
            String pattern = "(\\w+)=((\"(.+?)\")|(.+?)(?=\\s|$))";
            Pattern p = Pattern.compile(pattern);
            String line;                                                                                //bekommt in der while aktuelle Zeile übergeben
            bufferedReader.readLine();                                                                  //erste Zeile überspringen (digraph)
            Map<String, XYNode> nodeMap = new HashMap<>();

            //solange bufferedReader noch eine Zeile hat UND Zeile nicht leer ist (bricht nach Nodes ab)
            while (((line = bufferedReader.readLine()) != null) && (line.length() != 0) && (line.charAt(0) != '}')) {
                Map<String, String> map = new HashMap<>();                                                                  //hier werden immer alle matches gespeichert, als key dann x, y & label
                Matcher m = p.matcher(line);                                                                                //Zeile wird mit pattern durchsucht
                while (m.find()) {                                                                                          //Solange matcher was findet (x, y & label)
                    String value = m.group(2);
                    if (value.endsWith("]")) {                                                                              // "]" am ende wird entfernt
                        value = value.substring(0, value.length() - 1);
                        value = value.trim();
                    }
                    if (value.contains("\"")) {
                        value = value.replace("\"", "");
                    }
                    map.put(m.group(1), value);                                                     //group(1) ist immer Bezeichnung, also x,y oder label. Value ist dann wert
                }
                String id = line.substring(1, 4);
                id = id.trim();

                try {
                    XYNode node = new XYNode(map.get("label"), Double.parseDouble(map.get("x")), Double.parseDouble(map.get("y")));   //Hier wird XYNode erstellt

                    if (id.contains("[")) {
                        id = id.replace("[", "");
                        id = id.trim();
                    }

                    nodeMap.put(id, node);                                                                                           //Hier wird id des Knotens in Key und XYNode in value gespeichert
                    graph.addNode(node);                                                                                            //und zum graph hinzugefügt
                } catch (NullPointerException n) {
                    throw new ParseException("NullPointer");
                } catch (NumberFormatException nf) {
                    throw new ParseException("NumberFormat");
                }
            }


            //Edges
            while (((line = bufferedReader.readLine()) != null) && (line.length() != 0) && !line.contains("}")) {               //&& (line.charAt(0)!= '}')

                //Dist ziehen
                String value = "";
                Matcher m = p.matcher(line);
                while (m.find()) {                                                                                          //Solange matcher was findet (x, y & label)
                    value = m.group(2);
                    if (value.contains("]")) {
                        value = value.trim();
                        value = value.substring(0, value.length() - 1);
                        value = value.trim();
                    }
                }

                String pattern1 = "(\\d+)\\s+->\\s+(\\d+)";
                Pattern p1 = Pattern.compile(pattern1);
                Matcher m1 = p1.matcher(line);
                String from = "";
                String to = "";
                while (m1.find()) {
                    from = m1.group(1);
                    to = m1.group(2);
                }

                from = from.trim();
                to = to.trim();

                XYNode nodeFrom = nodeMap.get(from);
                XYNode nodeTo = nodeMap.get(to);
                graph.addEdge(nodeFrom, nodeTo, Optional.of(Double.parseDouble(value)));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    @Override
    public String write(TwoDimGraph graph) {
        StringBuilder sb = new StringBuilder();
        Map<String, Integer> nodeMap = new HashMap<>();

        sb.append("digraph{\n");
        int count = 1;
        for (XYNode node : graph.getNodes()) {
            String label = node.getLabel();
            String x = String.valueOf(node.getX());
            String y = String.valueOf(node.getY());
            nodeMap.put(node.getLabel(), count);

            sb.append("\t" + count + "[ label=" + label + " x=" + x + " y=" + y + "]\n");
            count++;
        }
        sb.append("\n");

        //Hier die Edges
        for (Edge<XYNode, Double> edge : graph.getEdges()) {
            String searchStart = edge.getStart().getLabel();
            String searchDest = edge.getDestination().getLabel();

            String idStart = String.valueOf(nodeMap.get(searchStart));
            String idDest = String.valueOf(nodeMap.get(searchDest));

            sb.append("\t" + idStart + " -> " + idDest + " [dist=1.0]\n");
        }
        sb.append("}\n");

        return sb.toString();
    }
}
