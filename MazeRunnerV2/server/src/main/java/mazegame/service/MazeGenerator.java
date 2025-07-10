package main.java.mazegame.service;

import java.awt.Point;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private final int rows, cols;
    private final char[][] maze;
    private final Random rand = new Random();
    public MazeGenerator(int rows,int cols) { this.rows=rows; this.cols=cols; maze=new char[rows][cols]; }
    public char[][] generate() {
        for(int r=0;r<rows;r++) for(int c=0;c<cols;c++) maze[r][c]='#';
        Stack<Point> st=new Stack<>(); Point s=new Point(1,1); maze[s.x][s.y]=' '; st.push(s);
        while(!st.isEmpty()){
            Point p=st.peek(),n=getNeighbor(p);
            if(n!=null){ maze[(p.x+n.x)/2][(p.y+n.y)/2]=' '; maze[n.x][n.y]=' '; st.push(n); }
            else st.pop();
        }
        char[][] copy=new char[rows][]; for(int i=0;i<rows;i++) copy[i]=maze[i].clone(); return copy;
    }
    private Point getNeighbor(Point p){ Point[] dirs={new Point(2,0),new Point(-2,0),new Point(0,2),new Point(0,-2)};
        for(int i=dirs.length-1;i>0;i--){int j=rand.nextInt(i+1);var t=dirs[i];dirs[i]=dirs[j];dirs[j]=t;}
        for(var d:dirs){int nx=p.x+d.x,ny=p.y+d.y; if(nx>0&&nx<rows&&ny>0&&ny<cols&&maze[nx][ny]=='#')return new Point(nx,ny);}return null;
    }
}
