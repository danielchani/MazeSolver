package main.java.algorithm;

import java.awt.Point;
import java.util.List;

// ממשק כללי לפתרון מבוך
public interface IMazeSolver {
    List<Point> solve(char[][] maze, Point start, Point end);
}
