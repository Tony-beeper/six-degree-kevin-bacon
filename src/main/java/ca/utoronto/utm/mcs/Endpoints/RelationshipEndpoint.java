package ca.utoronto.utm.mcs.Endpoints;

import ca.utoronto.utm.mcs.Neo4jDAO;
import ca.utoronto.utm.mcs.Utils;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RelationshipEndpoint {

    public static void handlePut(HttpExchange r, Neo4jDAO dao) throws IOException, JSONException {

        String body = Utils.convert(r.getRequestBody());
        try{
            JSONObject deserialized = new JSONObject(body);
            String actorId, movieId;
            if (deserialized.has("actorId")&& deserialized.has("movieId")){
                actorId = deserialized.getString("actorId");
                movieId = deserialized.getString("movieId");

            }else{
                r.sendResponseHeaders(400,-1);
                return;
            }
            try{
                int response = dao.addRelationship(actorId, movieId);
                r.sendResponseHeaders(response, -1);

            }catch (Exception e){
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }

        }catch(Exception e){
            e.printStackTrace();
            r.sendResponseHeaders(500,-1);


        }



    }
    public static void handleGet(HttpExchange r, Neo4jDAO dao) throws IOException, JSONException {

        String body = Utils.convert(r.getRequestBody());
        try{
            JSONObject deserialized = new JSONObject(body);
            String actorId, movieId;
            if (deserialized.has("actorId")&& deserialized.has("movieId")){
                actorId = deserialized.getString("actorId");
                movieId = deserialized.getString("movieId");

            }else{
                r.sendResponseHeaders(400,-1);
                return;
            }
            try{
                JSONObject json = dao.hasRelationship(actorId, movieId);
                System.out.println(json.toString());

                OutputStream os = r.getResponseBody();
                int response;
                if(json.has("actorId")) response = 200; else response = 404;

                r.sendResponseHeaders(response,json.toString().length());

                os.write(json.toString().getBytes(StandardCharsets.UTF_8));

                os.close();
            }catch (Exception e){
                r.sendResponseHeaders(500, -1);
                e.printStackTrace();
                return;
            }
            //r.sendResponseHeaders(200, -1);

        }catch(Exception e){
            e.printStackTrace();
            r.sendResponseHeaders(500,-1);


        }



    }
}
