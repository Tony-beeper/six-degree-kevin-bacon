package ca.utoronto.utm.mcs.Endpoints;

import ca.utoronto.utm.mcs.Neo4jDAO;
import ca.utoronto.utm.mcs.Utils;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaconEndpoint {

    public static void computeBaconNumber(HttpExchange r, Neo4jDAO dao) throws IOException, JSONException{

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

                JSONObject json = dao.computeBaconNumber(actorId);
                System.out.println(json.toString());
                OutputStream os = r.getResponseBody();
                int response;
                if(json.has("baconNumber")) response =200; else response = 404;
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
    public static void computeBaconPath(HttpExchange r, Neo4jDAO dao) throws IOException, JSONException{

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

                JSONObject json = dao.computeBaconPath(actorId);
                System.out.println(json.toString());

                OutputStream os = r.getResponseBody();
                int response;
                if(json.has("baconPath")) response =200; else response = 404;
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
