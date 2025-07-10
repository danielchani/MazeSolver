package main.java.mazegame.network;
import java.util.Map;
public class Response<T>{private Map<String,String>headers;private T body;public Response(){}public Response(Map<String,String>h,T b){headers=h;body=b;}public Map<String,String>getHeaders(){return headers;}public T getBody(){return body;}}
