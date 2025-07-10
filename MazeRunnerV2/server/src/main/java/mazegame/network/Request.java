package main.java.mazegame.network;
import java.util.Map;
public class Request<T>{private Map<String,String>headers;private T body;public Request(){}public Request(Map<String,String>h,T b){headers=h;body=b;}public Map<String,String>getHeaders(){return headers;}public T getBody(){return body;}}
