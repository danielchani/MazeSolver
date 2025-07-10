package main.java.mazegame.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.mazegame.network.Request;
import main.java.mazegame.network.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;

/**
 * Client for communicating with Maze Server via JSON over TCP.
 */
public class Client {
    private final String host;
    private final int port;
    private final Gson gson = new Gson();

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends a request with given headers and body,
     * and parses the response body as the given responseType.
     */
    public <T> T send(Map<String,String> headers, Object body, Type responseType) throws Exception {
        Request<Object> req = new Request<>(headers, body);
        String jsonReq = gson.toJson(req);

        try (Socket sock = new Socket(host, port);
             PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()))) {

            // Send request
            writer.println(jsonReq);

            // Read response
            String jsonRes = reader.readLine();
            Type respType = TypeToken.getParameterized(Response.class, responseType).getType();
            Response<T> res = gson.fromJson(jsonRes, respType);

            if (!"ok".equals(res.getHeaders().get("status"))) {
                throw new RuntimeException("Server error: " + res.getBody());
            }
            return res.getBody();
        }
    }
}