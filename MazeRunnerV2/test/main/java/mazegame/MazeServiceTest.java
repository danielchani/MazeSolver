package main.java.mazegame;

import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;
import main.java.mazegame.repository.MyDMFileImpl;
import main.java.mazegame.service.MazeService;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

public class MazeServiceTest {
    @Test
    public void testFullFlow() throws Exception {
        String tempPath = "testDataSource.dat";
        new File(tempPath).delete();

        IDao<MazeDataModel> dao = new MyDMFileImpl(tempPath);
        MazeService service = new MazeService(dao, 11, 11);

        service.createMaze("test");
        assertTrue(service.listMazeIds().contains("test"));

        List<?> path = service.solveMaze("test", "BFS");
        assertNotNull(path);
        MazeDataModel model = dao.find("test");
        assertNotNull(model.getPath());
        assertFalse(model.getPath().isEmpty());
    }
}