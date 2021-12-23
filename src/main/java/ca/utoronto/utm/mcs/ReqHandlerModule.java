package ca.utoronto.utm.mcs;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import dagger.Module;
import dagger.Provides;
import io.github.cdimascio.dotenv.Dotenv;
import org.neo4j.driver.*;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.reactive.RxSession;
import org.neo4j.driver.types.TypeSystem;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Module
public class ReqHandlerModule {
    public Neo4jDAO dao;

    private final String username = "neo4j";
    private final String password = "123456";
    // TODO Complete This Module
    @Provides
    Driver provideDriver(){
        Dotenv dotenv = Dotenv.load();
        String addr = dotenv.get("NEO4J_ADDR");
        String uriDb = String.format("bolt://%s:7687",addr);
        return GraphDatabase.driver(uriDb, AuthTokens.basic(this.username,this.password));
    }

    @Provides
    Neo4jDAO provideDao(Driver driver){
        return new Neo4jDAO(driver);
    }

    @Provides
    ReqHandler provideHandler(Neo4jDAO dao){
        return new ReqHandler(dao);
    }



}
