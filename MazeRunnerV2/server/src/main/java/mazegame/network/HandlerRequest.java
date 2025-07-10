package main.java.mazegame.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Method;
import java.util.Map;

public class HandlerRequest {
    private static final Gson gson = new Gson();

    public static String process(String jsonReq) {
        System.out.println("[Server] Received: " + jsonReq);
        Request<JsonObject> req = gson.fromJson(
                jsonReq,
                new TypeToken<Request<JsonObject>>() {}.getType()
        );

        String action = req.getHeaders().get("action");
        JsonObject body = req.getBody();

        try {
            Object ctrl = ControllerFactory.getController(action);

            // הוספנו מיפוי ל-generate ו-solve
            String methodName = switch (action) {
                case "maze/list"     ->  "list";
                case "maze/generate" ->  "generate";
                case "maze/solve"    ->  "solve";
                case "maze/reset"    ->  "reset";    // <—
                case "maze/join"     ->  "join";
                case "maze/move"     ->  "move";
                default -> throw new NoSuchMethodException("Unknown action: " + action);
            };

            // כל המתודות מקבלות Map<String,String>
            Method method = ctrl.getClass().getMethod(methodName, Map.class);
            @SuppressWarnings("unchecked")
            Object result = method.invoke(ctrl, gson.fromJson(body, Map.class));

            Response<Object> res = new Response<>(Map.of("status","ok"), result);
            String out = gson.toJson(res);
            System.out.println("[Server] OK: " + out);
            return out;

        } catch (Exception e) {
            e.printStackTrace();
            Response<String> err = new Response<>(
                    Map.of("status","error"),
                    e.toString()
            );
            String out = gson.toJson(err);
            System.out.println("[Server] ERR: " + out);
            return out;
        }
    }
}
