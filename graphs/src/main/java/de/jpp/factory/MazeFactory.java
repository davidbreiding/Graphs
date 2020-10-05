package de.jpp.factory;

import de.jpp.io.interfaces.GraphReader;
import de.jpp.maze.Maze;
import de.jpp.maze.MazeImpl;
import de.jpp.model.TwoDimGraph;
import de.jpp.model.XYNode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MazeFactory {


    /**
     * Creates a new empty maze with the specified width and height
     *
     * @param width  the width
     * @param height the height
     * @return a new empty maze with the specified width and height
     */
    public Maze getEmptyMaze(int width, int height) {
        return new MazeImpl(width, height);
    }

    /**
     * Returns a pixel representation of the specified maze
     *
     * @param maze the maze
     * @return a pixel representation of the specified maze
     */
    //Parameter geändert
    public BufferedImage getMazeAsImage(Maze maze) {

        MazeImpl maze1 = (MazeImpl) maze;

        BufferedImage bufferedImage = new BufferedImage(maze1.getWidth() * 2 + 1, maze1.getHeight() * 2 + 1, BufferedImage.TYPE_INT_RGB);

        //Alles schwarz
        boolean[][] res = new boolean[maze1.getHeight() * 2 + 1][maze1.getWidth() * 2 + 1];

        //alles bis auf Rand weiß und scharze Punkte
        for (int i = 1; i < res.length - 1; i++) {
            for (int j = 1; j < res[i].length - 1; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    res[i][j] = true;
                } else {
                    res[i][j] = false;
                }
            }
        }
        //vWalls einfügen
        int zeile1 = 2;
        for (int i = 0; i < maze1.getVWall().length; i++) {
            boolean[] wallArray = new boolean[maze1.getVWall()[i].length];

            for (int j = 0; j < maze1.getVWall()[i].length; j++) {
                wallArray[j] = maze1.getVWall()[i][j];
            }
            //In spalte sind walls einer zeile gespeichert
            int spalteRes = 1;
            for (int k = 0; k < wallArray.length; k++) {
                res[zeile1][spalteRes] = wallArray[k];
                spalteRes = spalteRes + 2;
            }
            zeile1 = zeile1 + 2;
        }

        //hWalls einfügen
        int zeile = 1;
        for (int i = 0; i < maze1.getHWall().length; i++) {
            boolean[] wallArray = new boolean[maze1.getHWall()[i].length];

            for (int j = 0; j < maze1.getHWall()[i].length; j++) {
                wallArray[j] = maze1.getHWall()[i][j];
            }
            //In spalte sind walls einer spalte gespeichert
            int spalteRes1 = 2;
            for (int k = 0; k < wallArray.length; k++) {
                res[zeile][spalteRes1] = wallArray[k];
                spalteRes1 = spalteRes1 + 2;
            }
            zeile = zeile + 2;
        }

        //Matrix in Image
        for (int i = 1; i < res.length - 1; i++) {
            for (int j = 1; j < res[i].length - 1; j++) {
                if (res[i][j] == false) {
                    bufferedImage.setRGB(j, i, Color.white.getRGB());
                } else {
                    bufferedImage.setRGB(j, i, Color.BLACK.getRGB());
                }
            }
        }

        return bufferedImage;
    }

    /**
     * Returns a random maze with specified width, height created by the specified algorithm specified from the instruction <br>
     * Random numbers are only taken from the specified random number generator (RNG) and only as specified in the instruction
     *
     * @param ran    the random number generator (RNG)
     * @param width  the width
     * @param height the height
     * @return a random maze with specified width and height
     */


    public static Maze getRandomMaze(Random ran, int width, int height) {

        MazeImpl maze = new MazeImpl(width, height);

        int maxHorizontal = height;
        int minHorizontal = 0;
        int maxVertikal = width;
        int minVertikal = 0;

        unterteileLabyrinth(maze, ran, maxVertikal, minVertikal, maxHorizontal, minHorizontal);
        return maze;

    }

    public static boolean unterteileLabyrinth(MazeImpl labyrinth, Random ran, int maxVertikal, int minVertikal, int maxHorizontal, int minHorizontal) {
        int rangeHorizontal = (maxHorizontal - minHorizontal);
        int rangeVertikal = (maxVertikal - minVertikal);

        //Abbruchbedingung
        if (rangeHorizontal <= 1 || rangeVertikal <= 1) {
            return false;
        } else if (rangeHorizontal > rangeVertikal) {
            UnterteileLabyrinthVertikal(labyrinth, ran, maxVertikal, minVertikal, maxHorizontal, minHorizontal);
        } else if (rangeVertikal > rangeHorizontal) {
            UnterteileLabyrinthHorizontal(labyrinth, ran, maxVertikal, minVertikal, maxHorizontal, minHorizontal);
        }
        //Falls quadratisch
        else {
            boolean wahrWert = ran.nextBoolean();
            if (wahrWert) {
                UnterteileLabyrinthHorizontal(labyrinth, ran, maxVertikal, minVertikal, maxHorizontal, minHorizontal);
            } else {
                UnterteileLabyrinthVertikal(labyrinth, ran, maxVertikal, minVertikal, maxHorizontal, minHorizontal);
            }
        }
        return true;
    }

    public static void UnterteileLabyrinthHorizontal(MazeImpl labyrinth, Random ran, int maxVertikal, int minVertikal, int maxHorizontal, int minHorizontal) {

        int rangeVertikal = maxVertikal - minVertikal;
        int rangeHorizontal = maxHorizontal - minHorizontal;

        int rnd1 = ran.nextInt(rangeVertikal - 1) + minVertikal;
        int rnd2 = ran.nextInt(rangeHorizontal - 1) + minHorizontal;

        for (int i = minHorizontal; i <= maxHorizontal - 1; i++) {
            if (i == rnd2) {
                continue;
            }
            labyrinth.setHWall(rnd1, i, true);
        }

        unterteileLabyrinth(labyrinth, ran, rnd1 + 1, minVertikal, maxHorizontal, minHorizontal);

        unterteileLabyrinth(labyrinth, ran, maxVertikal, rnd1 + 1, maxHorizontal, minHorizontal);
    }

    public static void UnterteileLabyrinthVertikal(MazeImpl labyrinth, Random ran, int maxVertikal, int minVertikal, int maxHorizontal, int minHorizontal) {

        int rangeHorizontal = maxHorizontal - minHorizontal;
        int rangeVertikal = maxVertikal - minVertikal;

        int rnd1 = ran.nextInt(rangeHorizontal - 1) + minHorizontal;
        int rnd2 = ran.nextInt(rangeVertikal - 1) + minVertikal;

        for (int i = minVertikal; i <= maxVertikal - 1; i++) {
            if (i == rnd2) {
                continue;
            }
            labyrinth.setVWall(i, rnd1, true);
        }

        unterteileLabyrinth(labyrinth, ran, maxVertikal, minVertikal, rnd1 + 1, minHorizontal);

        unterteileLabyrinth(labyrinth, ran, maxVertikal, minVertikal, maxHorizontal, rnd1 + 1);
    }

    /**
     * Returns a GraphReader which parses a TwoDimGraph from a Maze-Object
     *
     * @return a GraphReader which parses a TwoDimGraph from a Maze-Object
     */
    public GraphReader<XYNode, Double, TwoDimGraph, Maze> getMazeReader() {
        return null;
    }

}
