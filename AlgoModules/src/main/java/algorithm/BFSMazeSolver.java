package main.java.algorithm;

import java.awt.Point;
import java.util.*;

/**
 * מימוש אלגוריתם BFS למציאת המסלול הקצר ביותר
 */
public class BFSMazeSolver extends AbstractMazeSolver {

    @Override
    public List<Point> solve(char[][] maze, Point start, Point end) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> parent = new HashMap<>();

        queue.add(start);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.equals(end)) {
                return reconstructPath(parent, end);
            }

            for (int i = 0; i < DIRECTION_X.length; i++) {
                int newX = current.x + DIRECTION_X[i];
                int newY = current.y + DIRECTION_Y[i];

                if (isValid(maze, newX, newY, visited)) {
                    visited[newX][newY] = true;
                    Point next = new Point(newX, newY);
                    queue.add(next);
                    parent.put(next, current);
                }
            }
        }

        return Collections.emptyList();  // לא נמצא מסלול
    }
}
