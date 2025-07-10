// DFSMazeSolverTest.java
package main.java.algorithm;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;

public class DFSMazeSolverTest {

    private final DFSMazeSolver solver = new DFSMazeSolver();

    @Test
    public void testSolveWithPathExists() {
        // מבוך 4x4 עם נתיב עקלקל מ-(0,0) ל-(3,3)
        char[][] maze = {
                {' ', ' ', ' ', ' '},
                {' ', '#', '#', ' '},
                {' ', ' ', ' ', ' '},
                {'#', '#', ' ', ' '}
        };
        Point start = new Point(0, 0);
        Point end   = new Point(3, 3);

        List<Point> path = solver.solve(maze, start, end);
        assertFalse("Path should be found", path.isEmpty());
        assertEquals(start, path.get(0));
        assertEquals(end,   path.get(path.size() - 1));
        // וידוא שכל הצעדים אינם בתוך קיר
        for (Point p : path) {
            assertNotEquals("Path should not go through walls", '#', maze[p.x][p.y]);
        }
    }

    @Test
    public void testSolveWhenNoPath() {
        // מבוך חסום לחלוטין
        char[][] maze = {
                {' ', '#'},
                {'#', ' '}
        };
        Point start = new Point(0, 0);
        Point end   = new Point(1, 1);

        List<Point> path = solver.solve(maze, start, end);
        assertTrue("DFS should return empty list when no path exists", path.isEmpty());
    }
}
