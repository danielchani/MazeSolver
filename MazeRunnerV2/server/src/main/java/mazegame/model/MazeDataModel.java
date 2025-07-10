package main.java.mazegame.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;

public class MazeDataModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private char[][] grid;
    private List<Point> path;

    public MazeDataModel(String id, char[][] grid) {
        this.id = id;
        this.grid = grid;
    }
    public String getId() { return id; }
    public char[][] getGrid() { return grid; }
    public void setGrid(char[][] grid) { this.grid = grid; }
    public List<Point> getPath() { return path; }
    public void setPath(List<Point> path) { this.path = path; }
}
