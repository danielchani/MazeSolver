package main.java.algorithm;

import java.awt.Point;
import java.util.*;

/**
 * מחלקה אבסטרקטית שמיישמת חלק מהלוגיקה המשותפת לכל הפותרים של מבוך
 */
public abstract class AbstractMazeSolver implements IMazeSolver {
    // כיוונים: למעלה, למטה, שמאלה, ימינה
    protected static final int[] DIRECTION_X = {-1, 1, 0, 0};
    protected static final int[] DIRECTION_Y = {0, 0, -1, 1};

    /**
     * בודק אם הנקודה (x,y) בתוך גבולות המבוך, לא קיר, ולא כבר ביקרנו בה
     */
    protected boolean isValid(char[][] maze, int x, int y, boolean[][] visited) {
        int rows = maze.length;
        int cols = maze[0].length;
        return x >= 0 && x < rows
                && y >= 0 && y < cols
                && maze[x][y] != '#'
                && !visited[x][y];
    }

    /**
     * שחזור מסלול מה־Map של הורים (parent)
     */
    protected List<Point> reconstructPath(Map<Point, Point> parent, Point end) {
        LinkedList<Point> path = new LinkedList<>();
        Point current = end;
        while (current != null) {
            path.addFirst(current);
            current = parent.get(current);
        }
        return path;
    }

    @Override
    public abstract List<Point> solve(char[][] maze, Point start, Point end);
}
