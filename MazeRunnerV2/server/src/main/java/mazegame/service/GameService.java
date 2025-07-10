package main.java.mazegame.service;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;

import java.awt.Point;
import java.util.List;
import java.util.Map;

public class GameService {
    private final IDao<MazeDataModel> dao;
    private final MazeGenerator       gen;
    private final int rows, cols;
    private       GameState          state;  // ישמר לאורך קריאות

    public GameService(IDao<MazeDataModel> dao, int rows, int cols) {
        this.dao  = dao;
        this.gen  = new MazeGenerator(rows, cols);
        this.rows = rows;
        this.cols = cols;
    }

    private void ensureStarted(String id) throws Exception {
        if (state != null) return;

        char[][] grid;
        // אם אין id – גנרציה ושמירה, אחרת נטען מהקיים
        if (!dao.listIds().contains(id)) {
            grid = gen.generate();
            grid[1][0]           = ' ';
            grid[rows-2][cols-1] = ' ';
            dao.save(new MazeDataModel(id, grid));
        } else {
            grid = dao.find(id).getGrid();
        }
        state = new GameState(grid);
    }

    public Map<String,Object> join(String id) throws Exception {
        ensureStarted(id);
        String pid = state.join();
        return Map.of(
                "playerId", pid,
                "grid",      state.getGrid(),
                "players",   state.getPlayers()
        );
    }

    public Map<String,Point> move(String id, String playerId, String dir) throws Exception {
        ensureStarted(id);
        state.move(playerId, dir);
        return state.getPlayers();
    }

    public List<String> listMazeIds() throws Exception {
        return dao.listIds();
    }

    public void reset(String id) throws Exception {
        char[][] grid = gen.generate();
        grid[1][0]           = ' ';
        grid[rows-2][cols-1] = ' ';
        dao.save(new MazeDataModel(id, grid));
        state = new GameState(grid);
    }
}
