package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;

public class Server{
    HttpServer server;

    @Inject
    public Server(HttpServer http){
        this.server = http;


    }





}
