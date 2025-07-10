// MazeSolverContext.java
package main.java.algorithm;

import java.awt.Point;
import java.util.List;

/**
 *  מאפשר החלפה דינמית של אלגוריתם הפתרון
 */
public class MazeSolverContext {
    private IMazeSolver solver;

    public MazeSolverContext(IMazeSolver initialSolver) {
        this.solver = initialSolver;
    }

    public void setSolver(IMazeSolver solver) {
        this.solver = solver;
    }

    public List<Point> solve(char[][] maze, Point start, Point end) {
        return solver.solve(maze, start, end);
    }
}
