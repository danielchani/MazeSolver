// BFSMazeSolverTest.java
package main.java.algorithm;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;
import java.util.List;

public class BFSMazeSolverTest {

    private final BFSMazeSolver solver = new BFSMazeSolver();

    @Test
    public void testSolveSimpleStraightPath() {
        // מבוך פתוח 3x3, מסלול ישיר מ-(0,0) ל-(0,2)
        char[][] maze = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        Point start = new Point(0, 0);
        Point end   = new Point(0, 2);

        List<Point> path = solver.solve(maze, start, end);
        // מתחיל בנקודת start, מסתיים ב-end, אורך 3
        assertFalse("Path should not be empty", path.isEmpty());
        assertEquals(start, path.get(0));
        assertEquals(end,   path.get(path.size() - 1));
        assertEquals(3, path.size());
    }

    @Test
    public void testSolveNoPath() {
        // מבוך חסום: קיר מפריד בין ההתחלה והסוף
        char[][] maze = {
                {' ', '#', ' '},
                {'#', '#', '#'},
                {' ', '#', ' '}
        };
        Point start = new Point(0, 0);
        Point end   = new Point(2, 2);

        List<Point> path = solver.solve(maze, start, end);
        // לא קיים מסלול
        assertTrue("Path should be empty when no route exists", path.isEmpty());
    }
}
