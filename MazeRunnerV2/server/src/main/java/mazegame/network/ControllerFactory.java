// server/src/main/java/main/java/mazegame/network/ControllerFactory.java
package main.java.mazegame.network;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;
import main.java.mazegame.repository.MyDMFileImpl;
import main.java.mazegame.service.MazeService;
import main.java.mazegame.controller.MazeController;
import main.java.mazegame.service.GameService;
import main.java.mazegame.controller.GameController;

import java.io.IOException;

public class ControllerFactory {
    private static final IDao<MazeDataModel> dao;
    private static final MazeController      mazeCtrl;
    private static final GameController      gameCtrl;

    static {
        try {
            dao = new MyDMFileImpl("DataSource.txt");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize DAO", e);
        }
        // single-player
        MazeService msvc = new MazeService(dao, 41, 41);
        mazeCtrl        = new MazeController(dao, msvc);
        // multiplayer
        GameService gsvc = new GameService(dao, 41, 41);
        gameCtrl        = new GameController(dao, gsvc);
    }

    public static Object getController(String action) {
        return switch (action) {
            case "maze/list",
                 "maze/generate",
                 "maze/solve",
                 "maze/reset"   -> mazeCtrl;
            case "maze/join",
                 "maze/move"    -> gameCtrl;
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
