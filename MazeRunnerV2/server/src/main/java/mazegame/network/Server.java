package main.java.mazegame.network;
import java.io.*;import java.net.*;import java.util.Scanner;
public class Server implements Runnable{
    private final int port;public Server(int p){port=p;}
    @Override public void run(){try(ServerSocket ss=new ServerSocket(port)){System.out.println("Server listening on " + port);
        while(true){Socket c=ss.accept();new Thread(()->handle(c)).start();}}catch(Exception e){e.printStackTrace();}}
    private void handle(Socket s){try(var in=new Scanner(new InputStreamReader(s.getInputStream()));var out=new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true)){
        String req=in.nextLine();String res=HandlerRequest.process(req);out.println(res);
    }catch(Exception e){e.printStackTrace();}}
}
