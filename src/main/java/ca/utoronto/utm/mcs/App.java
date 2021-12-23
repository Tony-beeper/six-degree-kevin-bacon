package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dagger.internal.DaggerGenerated;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;

import javax.inject.Inject;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App
{
    static int port = 8080;

    public static void main(String[] args) throws IOException
    {
        // TODO Create Your Server Context Here, There Should Only Be One Context
        ServerComponent component1 = DaggerServerComponent.create();
        HttpServer server = component1.buildServer().server;
        ReqHandlerComponent component2 = DaggerReqHandlerComponent.create();
        server.createContext("/api/v1", component2.buildHandler());
        server.start();

        System.out.printf("Server started on port %d\n", port);


        // This code is used to get the neo4j address, you must use this so that we can mark :)
        Dotenv dotenv = Dotenv.load();
        String addr = dotenv.get("NEO4J_ADDR");
        System.out.println(addr);

    }
}
