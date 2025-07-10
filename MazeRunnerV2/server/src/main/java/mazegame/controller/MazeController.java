package main.java.mazegame.controller;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;
import main.java.mazegame.service.MazeService;

import java.awt.Point;
import java.util.List;
import java.util.Map;

public class MazeController {
    private final IDao<MazeDataModel> dao;
    private final MazeService service;

    public MazeController(IDao<MazeDataModel> dao, MazeService service) {
        this.dao     = dao;
        this.service = service;
    }

    /**
     * יוצר/מחדש מבוך:
     * אם id כבר קיים – מוחק קודם, ואז יוצר חדש
     */
    public MazeDataModel generate(Map<String,String> params) throws Exception {
        String id = params.get("id");
        // אם קיים – מוחקים כדי לא לקבל IllegalArgumentException ב-save
        if (dao.listIds().contains(id)) {
            dao.delete(id);
        }
        service.createMaze(id);
        return dao.find(id);
    }

    public List<Point> solve(Map<String,String> params) throws Exception {
        String id     = params.get("id");
        String solver = params.getOrDefault("solver","BFS");
        return service.solveMaze(id, solver);
    }

    public List<String> list(Map<String,String> params) throws Exception {
        return service.listMazeIds();
    }

    // action=“maze/reset” בסינגל־פלייר – פשוט ליצור מחדש
    public void reset(Map<String,String> p) throws Exception {
        // פשוט נועד לגנרציה מחדש
        generate(p);
    }
}
