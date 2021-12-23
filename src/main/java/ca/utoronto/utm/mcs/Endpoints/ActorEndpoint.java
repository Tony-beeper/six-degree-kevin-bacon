package ca.utoronto.utm.mcs.Endpoints;

import ca.utoronto.utm.mcs.Neo4jDAO;
import ca.utoronto.utm.mcs.Utils;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ActorEndpoint {



    public static void handlePut(HttpExchange r, Neo4jDAO dao) throws IOException, JSONException {

        String body = Utils.convert(r.getRequestBody());
        try{
            JSONObject deserialized = new JSONObject(body);
            String name, actorId;
            if (deserialized.has("name")&& deserialized.has("actorId")){
                name = deserialized.getString("name");
                actorId = deserialized.getString("actorId");

            }else{
                r.sendResponseHeaders(400,-1);
                return;
            }
            try{
                int response = dao.addActor(name, actorId);
                r.sendResponseHeaders(response, -1);
                return;

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
            String actorId;
            if (deserialized.has("actorId")){

                actorId = deserialized.getString("actorId");

            }else{
                r.sendResponseHeaders(400,-1);
                return;
            }
            try{

                JSONObject json = dao.getActor(actorId);
                System.out.println(json.toString());

                OutputStream os = r.getResponseBody();
                int response;
                if(json.has("name")) response = 200; else response = 404;
                r.sendResponseHeaders(response, json.toString().length());

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
