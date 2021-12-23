package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpServer;
import dagger.Module;
import dagger.Provides;

import java.io.IOException;
import java.net.InetSocketAddress;

@Module
public class ServerModule {
    // TODO Complete This Module
    static int port = 8080;
    @Provides
    HttpServer provideHttp() throws RuntimeException {
        try{
            return HttpServer.create(new InetSocketAddress("0.0.0.0", port),0);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        //return null;
    }

    @Provides
    Server server(HttpServer http){
        return new Server(http);
    }
}
