package main.java.mazegame.gui;

import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.mazegame.client.Client;
import main.java.mazegame.model.MazeDataModel;

import java.awt.Point;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class CombinedApp extends Application {
    private enum Mode { SINGLE, MULTI }

    private Client client;
    private Canvas canvas;
    private TextField idField;
    private ComboBox<String> solverChoice;
    private Button genBtn, solveBtn, resetBtn, joinBtn;
    private Label timerLabel;
    private Scene scene;

    private Mode mode = Mode.SINGLE;
    private MazeDataModel singleModel;
    private Point singlePos;
    private Timeline countdown;
    private int timeLeft;

    private String playerId;
    private char[][] grid;
    private Map<String,Point> players;

    @Override
    public void start(Stage stage) {
        client = new Client("localhost", 34567);

        // Menu
        MenuItem miSingle = new MenuItem("Single-player");
        MenuItem miMulti  = new MenuItem("Multiplayer");
        MenuItem miExit   = new MenuItem("Exit");
        miSingle.setOnAction(e -> switchMode(Mode.SINGLE));
        miMulti .setOnAction(e -> switchMode(Mode.MULTI));
        miExit  .setOnAction(e -> Platform.exit());
        Menu gameMenu = new Menu("Game");
        gameMenu.getItems().addAll(miSingle, miMulti, new SeparatorMenuItem(), miExit);
        MenuBar menuBar = new MenuBar(gameMenu);

        // Controls
        idField      = new TextField("level1");
        solverChoice = new ComboBox<>(FXCollections.observableArrayList("BFS","DFS"));
        solverChoice.setValue("BFS");
        genBtn   = styled(new Button("Generate"));
        solveBtn = styled(new Button("Solve"));
        resetBtn = styled(new Button("Restart"));
        joinBtn  = styled(new Button("Join"));

        HBox singleCtrls = new HBox(10,
                new Label("ID:"), idField, genBtn, solveBtn, resetBtn,
                new Label("Solver:"), solverChoice
        );
        HBox multiCtrls = new HBox(10,
                new Label("ID:"), idField, joinBtn, resetBtn
        );
        singleCtrls.setPadding(new Insets(10));
        multiCtrls.setPadding(new Insets(10));

        // Canvas and timer
        canvas = new Canvas(600,600);
        timerLabel = new Label();
        timerLabel.setStyle("-fx-text-fill: darkred; -fx-font-size:14px;");

        // Layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(canvas);
        root.setBottom(singleCtrls);   // start in SINGLE mode

        scene = new Scene(root, 650, 750);
        scene.getRoot().setStyle("-fx-background-color: BEIGE;");

        // Handlers
        genBtn  .setOnAction(e -> { doGenerate();  canvas.requestFocus(); });
        solveBtn.setOnAction(e -> { doSolve();     canvas.requestFocus(); });
        resetBtn.setOnAction(e -> { doReset();     canvas.requestFocus(); });
        joinBtn .setOnAction(e -> { doJoin();      canvas.requestFocus(); });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);

        stage.setScene(scene);
        stage.setTitle("Maze Game");
        stage.show();

        canvas.requestFocus();
    }

    private Button styled(Button b) {
        b.setStyle("-fx-background-color: lightblue; -fx-font-size: 14px;");
        b.setFocusTraversable(false);
        return b;
    }

    private void switchMode(Mode m) {
        mode = m;
        BorderPane root = (BorderPane)scene.getRoot();
        root.getChildren().removeIf(n -> n instanceof HBox);
        HBox ctrls = (m==Mode.SINGLE)
                ? new HBox(10, new Label("ID:"), idField, genBtn, solveBtn, resetBtn, new Label("Solver:"), solverChoice)
                : new HBox(10, new Label("ID:"), idField, joinBtn, resetBtn, timerLabel);
        ctrls.setPadding(new Insets(10));
        root.setBottom(ctrls);
        singleModel = null;
        playerId    = null;
        players     = null;
        if (countdown!=null) countdown.stop();
        timerLabel.setText("");
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        canvas.requestFocus();
    }

    // SINGLE PLAYER
    private void doGenerate() {
        try {
            singleModel = client.send(
                    Map.of("action","maze/generate"),
                    Map.of("id", idField.getText()),
                    MazeDataModel.class
            );
            grid = singleModel.getGrid();
            singlePos = new Point(1,0);
            redrawSingle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doSolve() {
        if (singleModel==null) return;
        try {
            Type t = new TypeToken<List<Point>>(){}.getType();
            List<Point> path = client.send(
                    Map.of("action","maze/solve"),
                    Map.of("id", idField.getText(), "solver", solverChoice.getValue()),
                    t
            );
            MazeCanvas.drawMaze(canvas, grid, path);
            drawLocalPlayer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void redrawSingle() {
        MazeCanvas.drawMaze(canvas, grid, null);
        drawLocalPlayer();
    }

    private void drawLocalPlayer() {
        var gc = canvas.getGraphicsContext2D();
        double cw = canvas.getWidth()/grid[0].length;
        double ch = canvas.getHeight()/grid.length;
        gc.setFill(Color.GREEN);
        gc.fillOval(singlePos.y*cw, singlePos.x*ch, cw, ch);
    }

    private void doReset() {
        try {
            client.send(
                    Map.of("action","maze/reset"),
                    Map.of("id", idField.getText()),
                    Object.class
            );
            singleModel = null;
            playerId    = null;
            players     = null;
            if (countdown!=null) countdown.stop();
            timerLabel.setText("");
            canvas.getGraphicsContext2D().clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // MULTIPLAYER
    @SuppressWarnings("unchecked")
    private void doJoin() {
        if (mode!=Mode.MULTI) return;
        try {
            Type t = new TypeToken<Map<String,Object>>(){}.getType();
            Map<String,Object> resp = client.send(
                    Map.of("action","maze/join"),
                    Map.of("id", idField.getText()),
                    t
            );
            playerId = (String)resp.get("playerId");
            var gl = (List<List<String>>)resp.get("grid");
            int R=gl.size(), C=gl.get(0).size();
            grid = new char[R][C];
            for(int r=0;r<R;r++)
                for(int c=0;c<C;c++)
                    grid[r][c] = gl.get(r).get(c).charAt(0);
            players = (Map<String,Point>)resp.get("players");
            startTimer(60);
            redrawMulti();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void redrawMulti() {
        MazeCanvas.drawMaze(canvas, grid, null);
        var gc = canvas.getGraphicsContext2D();
        double cw = canvas.getWidth()/grid[0].length;
        double ch = canvas.getHeight()/grid.length;
        for(var e:players.entrySet()){
            gc.setFill(e.getKey().equals(playerId)?Color.BLUE:Color.RED);
            Point p = e.getValue();
            gc.fillOval(p.y*cw, p.x*ch, cw, ch);
        }
    }

    private void startTimer(int secs) {
        timeLeft = secs;
        timerLabel.setText("Time: "+timeLeft);
        if(countdown!=null) countdown.stop();
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), ev-> {
            timeLeft--;
            timerLabel.setText("Time: "+timeLeft);
            if(timeLeft<=0) countdown.stop();
        }));
        countdown.setCycleCount(secs);
        countdown.play();
    }

    // KEY HANDLING
    private void onKeyPressed(KeyEvent ev) {
        if (mode==Mode.SINGLE && singleModel!=null) {
            handleSingleKey(ev.getCode());
            ev.consume();
        }
        else if (mode==Mode.MULTI && playerId!=null && timeLeft>0) {
            handleMultiKey(ev.getCode());
            ev.consume();
        }
    }

    private void handleSingleKey(KeyCode code) {
        int dx=0, dy=0;
        switch(code) {
            case UP:    dx=-1; break;
            case DOWN:  dx=1;  break;
            case LEFT:  dy=-1; break;
            case RIGHT: dy=1;  break;
            default: return;
        }
        int nx=singlePos.x+dx, ny=singlePos.y+dy;
        if(nx>=0 && nx<grid.length && ny>=0 && ny<grid[0].length
                && grid[nx][ny]==' ') {
            singlePos.setLocation(nx, ny);
            redrawSingle();
        }
    }

    private void handleMultiKey(KeyCode code) {
        String dir = null;
        if (playerId.equals("P1")) {
            switch(code) {
                case UP: dir="UP"; break;
                case DOWN:dir="DOWN";break;
                case LEFT:dir="LEFT";break;
                case RIGHT:dir="RIGHT";break;
            }
        }
        if (playerId.equals("P2")) {
            switch(code) {
                case W: dir="UP";    break;
                case S: dir="DOWN";  break;
                case A: dir="LEFT";  break;
                case D: dir="RIGHT"; break;
            }
        }
        if (dir!=null) {
            try {
                Type t = new TypeToken<Map<String,Point>>(){}.getType();
                players = client.send(
                        Map.of("action","maze/move"),
                        Map.of("id", idField.getText(),
                                "playerId", playerId,
                                "dir", dir),
                        t
                );
                redrawMulti();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
