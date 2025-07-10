// Main.java (עודכן לשימוש ב-MazeSolverContext)
package main.java.algorithm;

import java.awt.Point;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        if (args.length != 6) {
            System.out.println("Usage: java -jar MazeSolver.jar [BFS|DFS] <inputFile> <startX> <startY> <endX> <endY>");
            System.exit(1);
        }

        String type     = args[0];
        String filePath = args[1];
        int sx = Integer.parseInt(args[2]);
        int sy = Integer.parseInt(args[3]);
        int ex = Integer.parseInt(args[4]);
        int ey = Integer.parseInt(args[5]);

        // קריאה מהקובץ
        char[][] maze;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            int rows = lines.size();
            int cols = lines.get(0).length();
            maze = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                maze[i] = lines.get(i).toCharArray();
            }
        } catch (IOException e) {
            System.err.println("Error reading maze file: " + e.getMessage());
            return;
        }

        // יצירת Context עם האלגוריתם הרצוי
        MazeSolverContext context;
        if ("BFS".equalsIgnoreCase(type)) {
            context = new MazeSolverContext(new BFSMazeSolver());
        } else if ("DFS".equalsIgnoreCase(type)) {
            context = new MazeSolverContext(new DFSMazeSolver());
        } else {
            System.err.println("Unknown solver type: " + type);
            return;
        }

        // פתרון והדפסה
        List<Point> path = context.solve(maze, new Point(sx, sy), new Point(ex, ey));
        if (path.isEmpty()) {
            System.out.println("No path found.");
        } else {
            System.out.println("Path:");
            path.forEach(p -> System.out.println("(" + p.x + "," + p.y + ")"));
        }
    }
}
