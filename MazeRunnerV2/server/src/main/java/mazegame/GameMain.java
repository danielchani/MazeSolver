package main.java.mazegame;
import main.java.mazegame.model.MazeDataModel;
import main.java.mazegame.repository.IDao;
import main.java.mazegame.repository.MyDMFileImpl;
import main.java.mazegame.service.MazeService;
import java.awt.Point;import java.util.List;
public class GameMain{
    public static void main(String[]args)throws Exception{
        IDao<MazeDataModel> dao=new MyDMFileImpl("DataSource.txt");
        MazeService svc=new MazeService(dao,21,21);
        String id="level1";
        if(!svc.listMazeIds().contains(id)){svc.createMaze(id);}else{dao.delete(id);svc.createMaze(id);System.out.println("Re-created: " + id);}
        List<Point> path=svc.solveMaze(id,args.length>0?args[0]:"BFS");System.out.println("Path len="+path.size());
        MazeDataModel m=dao.find(id);
        for(char[]r:m.getGrid())System.out.println(new String(r));
    }
}
