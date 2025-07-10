package main.java.mazegame.service;

import java.awt.Point;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private final char[][] grid;
    private final Map<String,Point> players = new ConcurrentHashMap<>();

    public GameState(char[][] grid) {
        this.grid = grid;
    }

    public char[][] getGrid() {
        return grid;
    }

    /** מייצר מזהה חדש, מוסיף ל־players ומחזיר את המזהה והנקודה ההתחלתית */
    public synchronized String join() {
        String pid = "P" + (players.size()+1);
        Point start = new Point(1,0);
        players.put(pid, start);
        return pid;
    }

    /** מנסה להזיז את השחקן, אם המהלך חוקי */
    public synchronized void move(String pid, String dir) {
        Point p = players.get(pid);
        if (p==null) return;
        int dx=0, dy=0;
        switch(dir) {
            case "UP":    dx=-1; break;
            case "DOWN":  dx= 1; break;
            case "LEFT":  dy=-1; break;
            case "RIGHT": dy= 1; break;
        }
        int nx = p.x + dx, ny = p.y + dy;
        if (nx<0||nx>=grid.length||ny<0||ny>=grid[0].length) return;
        if (grid[nx][ny] == ' ') {
            players.put(pid, new Point(nx,ny));
        }
    }

    /** מחזיר snapshot של כל השחקנים */
    public Map<String,Point> getPlayers() {
        return Map.copyOf(players);
    }
}
