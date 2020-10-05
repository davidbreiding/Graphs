package de.jpp.maze;

import java.util.Arrays;
import java.util.Objects;

public class MazeImpl implements Maze {

    int width;
    int height;
    boolean[][] maze;
    boolean[][] hWall;
    boolean[][] vWall;


    public MazeImpl(int width, int height) {
        this.width = width;
        this.height = height;
        this.maze = new boolean[height][width];
        if (width > 0) {
            hWall = new boolean[height][width - 1];
        } else {
            hWall = new boolean[height][width];
        }
        if (height > 0) {
            vWall = new boolean[height - 1][width];
        } else {
            vWall = new boolean[height][width];
        }
    }

    public static boolean[][] mazeToMatrix(MazeImpl maze1) {
        //Alles schwarz
        boolean[][] res = new boolean[maze1.getHeight() * 2 + 1][maze1.getWidth() * 2 + 1];

        //alles bis auf Rand weiß
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
        return res;
    }

    @Override
    public void setHWall(int x, int y, boolean wallActive) {
        try {
            hWall[y][x] = wallActive;
        } catch (IndexOutOfBoundsException i) {
            System.out.println("Index out of Bounds setHWall");
        }
    }

    @Override
    public void setVWall(int x, int y, boolean wallActive) {
        try {
            vWall[y][x] = wallActive;
        } catch (IndexOutOfBoundsException i) {
            System.out.println("Index out of Bounds setVWall");
        }
    }

    //Hier nochmal
    @Override
    public void setAllWalls(boolean wallActive) {
        for (int i = 0; i < hWall.length; i++) {
            for (int j = 0; j < hWall[i].length; j++) {
                hWall[i][j] = wallActive;
            }
        }
        for (int i = 0; i < vWall.length; i++) {
            for (int j = 0; j < vWall[i].length; j++) {
                vWall[i][j] = wallActive;
            }
        }
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public boolean[][] getHWall() {
        return hWall;
    }

    public boolean[][] getVWall() {
        return vWall;
    }

    public boolean[][] getMaze() {
        return maze;
    }

    @Override
    public boolean isHWallActive(int x, int y) {
        try {
            return hWall[y][x];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean isVWallActive(int x, int y) {
        try {
            return vWall[y][x];
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeImpl maze1 = (MazeImpl) o;
        if ((this.width != maze1.width) || (this.height != maze1.height)) {
            return false;
        }

        for (int i = 0; i < this.hWall.length; i++) {
            for (int j = 0; j < this.hWall[i].length; j++) {
                if (this.hWall[i][j] != maze1.hWall[i][j]) {
                    return false;
                }
            }
        }

        for (int i = 0; i < this.vWall.length; i++) {
            for (int j = 0; j < this.vWall[i].length; j++) {
                if (this.vWall[i][j] != maze1.vWall[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.hashCode(maze);
        result = 31 * result + Arrays.hashCode(hWall);
        result = 31 * result + Arrays.hashCode(vWall);
        return result;
    }
}
