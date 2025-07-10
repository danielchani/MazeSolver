package main.java.algorithm;

import java.awt.Point;
import java.util.*;

/**
 * מימוש אלגוריתם DFS למציאת מסלול כלשהו (לא בהכרח הקצר ביותר)
 */
public class DFSMazeSolver extends AbstractMazeSolver {

    @Override
    public List<Point> solve(char[][] maze, Point start, Point end) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        List<Point> path = new ArrayList<>();
        dfs(maze, start, end, visited, path);
        return path;
    }

    private boolean dfs(char[][] maze, Point current, Point end, boolean[][] visited, List<Point> path) {
        int x = current.x, y = current.y;
        if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length
                || maze[x][y] == '#' || visited[x][y]) {
            return false;
        }

        visited[x][y] = true;
        path.add(current);

        if (current.equals(end)) {
            return true;
        }

        for (int i = 0; i < DIRECTION_X.length; i++) {
            int newX = x + DIRECTION_X[i];
            int newY = y + DIRECTION_Y[i];
            if (dfs(maze, new Point(newX, newY), end, visited, path)) {
                return true;
            }
        }

        // אם לא הובלנו לסיום – מסירים את הצעד האחרון וממשיכים לחפש
        path.remove(path.size() - 1);
        return false;
    }
}
