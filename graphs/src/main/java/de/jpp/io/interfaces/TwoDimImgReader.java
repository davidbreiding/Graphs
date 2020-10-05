package de.jpp.io.interfaces;

import de.jpp.model.GraphImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TwoDimImgReader implements GraphReader<XYNode, Double, TwoDimGraph, BufferedImage> {
    @Override
    public TwoDimGraph read(BufferedImage input) throws ParseException {
        TwoDimGraph twoDimGraph = new TwoDimGraph(new GraphImpl());


        int[][] matrix = new int[input.getHeight()][input.getWidth()];

        for (int i = 0; i < input.getHeight(); i++) {
            for (int j = 0; j < input.getWidth(); j++) {
                Color color = new Color(input.getRGB(j, i));
                float[] kp = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                if (kp[2] < 0.5) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = 1;
                }
            }
        }

        Map<String, XYNode> nodeMap = new HashMap<>();

//Nodes
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    XYNode node = new XYNode("(" + j + "|" + i + ")", j, i);
                    twoDimGraph.addNode(node);
                    nodeMap.put(node.getLabel(), node);
                }
            }
        }


        //Edges
        for (int i = 1; i < matrix.length - 1; i++) {
            for (int j = 1; j < matrix[i].length - 1; j++) {
                if (matrix[i][j] == 1) {
                    XYNode nodeFrom = nodeMap.get("(" + String.valueOf(j) + "|" + String.valueOf(i) + ")");

                    if (matrix[i - 1][j] == 1) {
                        XYNode nodeTo = nodeMap.get("(" + String.valueOf(j) + "|" + String.valueOf(i - 1) + ")");
                        twoDimGraph.addEdge(nodeFrom, nodeTo, Optional.of(1.0));
                    }

                    if (matrix[i][j - 1] == 1) {
                        XYNode nodeTo = nodeMap.get("(" + String.valueOf(j - 1) + "|" + String.valueOf(i) + ")");
                        twoDimGraph.addEdge(nodeFrom, nodeTo, Optional.of(1.0));
                    }
                    if (matrix[i][j + 1] == 1) {
                        XYNode nodeTo = nodeMap.get("(" + String.valueOf(j + 1) + "|" + String.valueOf(i) + ")");
                        twoDimGraph.addEdge(nodeFrom, nodeTo, Optional.of(1.0));
                    }
                    if (matrix[i + 1][j] == 1) {
                        XYNode nodeTo = nodeMap.get("(" + String.valueOf(j) + "|" + String.valueOf(i + 1) + ")");
                        twoDimGraph.addEdge(nodeFrom, nodeTo, Optional.of(1.0));
                    }
                } else {
                    //Falls es nicht weiß ist
                    continue;
                }
            }
        }
        return twoDimGraph;
    }
}
