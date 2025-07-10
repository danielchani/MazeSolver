package main.java.mazegame.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.awt.Point;
import java.util.List;

public class MazeCanvas {
    public static void drawMaze(Canvas canvas, char[][] grid, List<Point> path) {
        var gc = canvas.getGraphicsContext2D();
        int rows = grid.length, cols = grid[0].length;
        double cw = canvas.getWidth()  / cols;
        double ch = canvas.getHeight() / rows;

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // ציור הקירות והרצפה
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                gc.setFill(grid[r][c] == '#' ? Color.BLACK : Color.WHITE);
                gc.fillRect(c * cw, r * ch, cw, ch);
            }
        }

        // סימון נתיב אדום
        if (path != null) {
            gc.setFill(Color.RED);
            for (Point p : path) {
                double x = p.y * cw;
                double y = p.x * ch;
                gc.fillOval(x, y, cw, ch);
            }
        }

        // סימון כניסה (ירוק) ויציאה (כתום)
        gc.setFill(Color.LIME);
        gc.fillOval(0 * cw, 1 * ch, cw, ch);                 // entrance at (1,0)
        gc.setFill(Color.ORANGE);
        gc.fillOval((cols-1) * cw, (rows-2) * ch, cw, ch);   // exit at (rows-2,cols-1)
    }
}
