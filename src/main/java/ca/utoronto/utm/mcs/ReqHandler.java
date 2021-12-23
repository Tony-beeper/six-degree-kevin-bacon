package ca.utoronto.utm.mcs;

import java.io.IOException;

import ca.utoronto.utm.mcs.Endpoints.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ReqHandler implements HttpHandler {

    // TODO Complete This Class
    public Neo4jDAO dao;
    public ReqHandler(Neo4jDAO dao) {
        this.dao = dao;
    }


    public void handle(HttpExchange r) throws IOException {
        try{
            String URI = r.getRequestURI().toString();
            switch (r.getRequestMethod()) {
                case "PUT":
                    if (URI.equals("/api/v1/addActor")) {
                        System.out.println("addActor");
                        ActorEndpoint.handlePut(r, dao);
                    } else if (URI.equals("/api/v1/addMovie")) {
                        System.out.println("addMovie");
                        MovieEndpoint.handlePut(r, dao);
                    } else if (URI.equals("/api/v1/addRelationship")) {
                        System.out.println("addingRelationship");
                        RelationshipEndpoint.handlePut(r, dao);
                    };

                case "GET":
                    if (URI.equals("/api/v1/getActor")) {
                        System.out.println("getActor");
                        ActorEndpoint.handleGet(r, dao);
                    }else if(URI.equals("/api/v1/getMovie")){
                        System.out.println("getMovie");
                        MovieEndpoint.handleGet(r, dao);
                    }else if(URI.equals("/api/v1/hasRelationship")){
                        System.out.println("hasRelationship");
                        RelationshipEndpoint.handleGet(r, dao);
                    }else if(URI.equals("/api/v1/computeBaconNumber")){
                        System.out.println("computeBaconNumber");
                        BaconEndpoint.computeBaconNumber(r, dao);
                    }else if(URI.equals("/api/v1/computeBaconPath")){
                        System.out.println("computeBaconPath");
                        BaconEndpoint.computeBaconPath(r, dao);

                    }
                case "DELETE":
                    if (URI.equals("/api/v1/clearNodes")){
                        System.out.println("clearNodes");
                        ClearEndpoint.clearNodes(r, dao);
                    }


                    break;

            }


        }catch (Exception e){
            e.printStackTrace();
        }
        
    }



}