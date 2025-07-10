package main.java.mazegame.controller;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;
import main.java.mazegame.service.GameService;

import java.awt.Point;
import java.util.List;
import java.util.Map;

/**
 * Controller שמתממשק ל־GameService דרך action names.
 */
public class GameController {
    private final IDao<MazeDataModel> dao;
    private final GameService service;

    public GameController(IDao<MazeDataModel> dao, GameService service) {
        this.dao     = dao;
        this.service = service;
    }

    // action = "maze/list"
    public List<String> list(Map<String,String> params) throws Exception {
        return service.listMazeIds();
    }

    // action = "maze/join"
    public Map<String,Object> join(Map<String,String> params) throws Exception {
        return service.join(params.get("id"));
    }

    // action = "maze/move"
    public Map<String,Point> move(Map<String,String> params) throws Exception {
        return service.move(
                params.get("id"),
                params.get("playerId"),
                params.get("dir")
        );
    }

    public void reset(Map<String,String> p) throws Exception {
        service.reset(p.get("id"));
    }
}
