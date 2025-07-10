// MainApp.java
package main.java.mazegame.gui;

import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.java.mazegame.client.Client;
import main.java.mazegame.model.MazeDataModel;

import java.awt.Point;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class MainApp extends Application {
    private Client client;
    private Canvas canvas;
    private TextField idField, solverField;
    private MazeDataModel currentModel;

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialize client
        client = new Client("localhost", 34567);

        // 2. Build UI controls
        idField     = new TextField("level1");
        solverField = new TextField("BFS");
        Button genBtn   = new Button("Generate");
        Button solveBtn = new Button("Solve");

        HBox controls = new HBox(10,
                new Label("ID:"), idField,
                genBtn,
                new Label("Solver:"), solverField,
                solveBtn
        );
        controls.setPadding(new Insets(10));

        canvas = new Canvas(420, 420);
        VBox root = new VBox(10, controls, canvas);
        root.setPadding(new Insets(10));

        // 3. Wire up actions
        genBtn.setOnAction(e -> {
            String id = idField.getText();
            System.out.println("[Client] Generating maze: " + id);
            try {
                currentModel = client.send(
                        Map.of("action","maze/generate"),
                        Map.of("id", id),
                        MazeDataModel.class
                );
                System.out.println("[Client] Received grid size: " + currentModel.getGrid().length);
                MazeCanvas.drawMaze(canvas, currentModel.getGrid(), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        solveBtn.setOnAction(e -> {
            if (currentModel == null) {
                System.err.println("[Client] Please generate a maze first!");
                return;
            }
            String solver = solverField.getText();
            System.out.println("[Client] Solving maze with " + solver);
            try {
                Type pathType = new TypeToken<List<Point>>() {}.getType();
                List<Point> path = client.send(
                        Map.of("action","maze/solve"),
                        Map.of("id", idField.getText(), "solver", solver),
                        pathType
                );
                System.out.println("[Client] Received path length: " + path.size());
                MazeCanvas.drawMaze(canvas, currentModel.getGrid(), path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 4. Show window
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Maze Client");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
