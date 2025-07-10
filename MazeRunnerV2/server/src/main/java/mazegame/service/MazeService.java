package main.java.mazegame.service;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;

import java.awt.Point;
import java.util.List;

// ייבוא מה־AlgoModules.jar:
import main.java.algorithm.IMazeSolver;
import main.java.algorithm.BFSMazeSolver;
import main.java.algorithm.DFSMazeSolver;
import main.java.algorithm.MazeSolverContext;

public class MazeService {
    private final IDao<MazeDataModel> dao;
    private final int rows, cols;

    public MazeService(IDao<MazeDataModel> dao, int rows, int cols) {
        this.dao  = dao;
        this.rows = rows;
        this.cols = cols;
    }

    public void createMaze(String id) throws Exception {
        char[][] grid = new MazeGenerator(rows, cols).generate();

        // פתחים: כניסה בשמאל ויציאה בימין
        grid[1][0]             = ' ';
        grid[rows - 2][cols-1] = ' ';

        dao.save(new MazeDataModel(id, grid));
    }

    public List<String> listMazeIds() throws Exception {
        return dao.listIds();
    }

    public List<Point> solveMaze(String id, String solverType) throws Exception {
        MazeDataModel model = dao.find(id);
        char[][] grid = model.getGrid();

        // הגדרת נקודות כניסה/יציאה
        Point start = new Point(1, 0);
        Point goal  = new Point(rows - 2, cols - 1);

        // בוחרים solver מתוך AlgoModules
        IMazeSolver solver = "DFS".equalsIgnoreCase(solverType)
                ? new DFSMazeSolver()
                : new BFSMazeSolver();

        MazeSolverContext context = new MazeSolverContext(solver);
        List<Point> path = context.solve(grid, start, goal);

        // שמירת הנתיב ו-return
        model.setPath(path);
        dao.update(model);
        return path;
    }
}
