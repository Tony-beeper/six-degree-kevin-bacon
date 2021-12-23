package ca.utoronto.utm.mcs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.List;

// All your database transactions or queries should
// go in this class
public class Neo4jDAO {
    // TODO Complete This Class
    private final Session session;
    private final Driver driver;

    public Neo4jDAO(Driver dri) {
        System.out.println("DAO successfully Start");
        this.driver = dri;

        this.session = driver.session();

    }
    public boolean checkActorExistence(String id){
        String query = "MATCH (a:actor{id:\"%s\"}) RETURN (count(a) = 0) as p";
        query = String.format(query,id);
        Result res = this.session.run(query);
        return res.next().get("p").asBoolean();

    }
    public boolean checkMovieExistence(String id){
        String query = "MATCH (m:movie{id:\"%s\"}) RETURN (count(m) = 0) as p";
        query = String.format(query,id);
        Result res = this.session.run(query);
        return res.next().get("p").asBoolean();

    }
    public void clearNodes(String clear){
        String query;
        query = "MATCH (n) DETACH DELETE n";
        this.session.run(query);
        return;
    }

    public int addActor(String name, String id){

        boolean test = checkActorExistence(id);
        if(test) {
            System.out.println("No bacon, add Bacon");
            String query;
            query = "CREATE (n:actor {name: \"%s\", id: \"%s\"})";
            query = String.format(query, name, id);
            this.session.run(query);
            return 200;
        }
        return 400;
    }
    public int addMovie(String name, String movieId){

        boolean test = checkMovieExistence(movieId);
        if(test) {
            String query;
            query = "CREATE (n:movie {name: \"%s\", id: \"%s\"})";
            query = String.format(query, name, movieId);
            this.session.run(query);
            return 200;
        }
        return 400;
    }

    public int addRelationship(String actorId, String movieId){
        String query;
        query = "MATCH (a:actor), (m:movie) \n" +
                "WHERE a.id = \"%s\" AND m.id = \"%s\"\n" +
                "CREATE (a)-[r:ACTED_IN]->(m)\n" +
                "RETURN count(a)+count(m) = 2 as p";
        query = String.format(query, actorId, movieId);
        Result result = this.session.run(query);
        boolean tes = result.next().get("p").asBoolean();
        if(tes){
            return 200;
        }else{
            return 404;
        }

    }
/*
needs to be fixed, can't return without movie involved
 */
    public JSONObject getActor(String actorId) throws JSONException {
        String query;
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        String query1 = "MATCH (a:actor{id:\"%s\"}) \n RETURN a.name";
        query1 = String.format(query1,actorId);
        Result res = this.session.run(query1);
        if(res.hasNext()){
            Record name = res.next();
            json.put("name", name.get("a.name").asString());
        }


        query = "MATCH (a:actor{id:\"%s\"}) \n WITH a \n MATCH(a)-[:ACTED_IN]->(m:movie) \n RETURN a.name, collect(m.id) as movieIds";
        query = String.format(query, actorId);
        Result result = this.session.run(query);
        json.put("actorId", actorId);

        while (result.hasNext()){
            Record str = result.next();
            List<Object> movieIds = str.get("movieIds").asList();
            for(Object id: movieIds){
                array.put(id);
            }
            json.put("movies",array);

            return json;

        }
        json.put("movies",array);
        return json;

    }
    /*
    Fixed, movie won't be returned with 0 actors
     */
    public JSONObject getMovie(String movieId) throws JSONException {
        String query;
        //Needs to be Fixed
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();


        String query1 = "MATCH (m:movie{id:\"%s\"}) \n RETURN m.name";
        query1 = String.format(query1,movieId);
        Result res = this.session.run(query1);

        if(res.hasNext()){
            Record name = res.next();
            json.put("name", name.get("m.name").asString());
        }


        query = "MATCH (m:movie{id:\"%s\"}) \n WITH m \n MATCH(m)<-[:ACTED_IN]-(a:actor) \n RETURN m.name, collect(a.id) as actorIds";
        query = String.format(query, movieId);
        Result result = this.session.run(query);
        json.put("movieId", movieId);

        while (result.hasNext()){
            Record str = result.next();
            List<Object> movieIds = str.get("actorIds").asList();
            for(Object id: movieIds){
                array.put(id);
            }
            json.put("actors",array);

            return json;

        }
        json.put("actors",array);
        return json;

    }
    public JSONObject hasRelationship(String actorId, String movieId) throws JSONException {
        String query;
        JSONObject json = new JSONObject();
        String query1 = "MATCH (a:actor{id:\"%s\"}),(m:movie{id:\"%s\"})\nRETURN count(a)+count(m) = 2 as p";
        query1 = String.format(query1, actorId, movieId);
        Result res = this.session.run(query1);
        if(res.next().get("p").asBoolean()) {

            query = "RETURN (EXISTS((:actor{id:\"%s\"})-[:ACTED_IN]->(:movie{id:\"%s\"}))) as bool";
            query = String.format(query, actorId, movieId);
            Result result = this.session.run(query);


            while (result.hasNext()) {
                Record str = result.next();
                boolean hasRelationship = str.get("bool").asBoolean();

                json.put("actorId", actorId);
                json.put("movieId", movieId);
                json.put("hasRelationship", hasRelationship);
                return json;

            }
        }

        return json;

    }
    public JSONObject computeBaconNumber(String actorId) throws JSONException {
        String query;
        JSONObject json = new JSONObject();
        String query1 = "MATCH (a:actor{id:\"%s\"}) \nRETURN count(a) = 1 as p";
        query1 = String.format(query1, actorId);
        Result res = this.session.run(query1);
        if(res.next().get("p").asBoolean()) {
            if (actorId.equals("nm0000102")) {
                json.put("baconNumber", 0);

            } else {
                query = "MATCH path = shortestPath((:actor{id:\"%s\"})-[*]-(:actor{id: \"nm0000102\"}))\n RETURN length(path) as length";
                query = String.format(query, actorId);
                Result result = this.session.run(query);


                while (result.hasNext()) {
                    Record str = result.next();
                    int length = str.get("length").asInt();


                    json.put("baconNumber", length / 2);
                    return json;

                }
            }
        }
        return json;


    }
    public JSONObject computeBaconPath(String actorId) throws JSONException {
        String query;
        JSONObject json = new JSONObject();
        String query1 = "MATCH (a:actor{id:\"%s\"}) \nRETURN count(a) = 1 as p";
        query1 = String.format(query1, actorId);
        Result rus = this.session.run(query1);
        JSONArray array = new JSONArray();

        if(rus.next().get("p").asBoolean()) {


            if (actorId.equals("nm0000102")) {
                array.put("nm0000102");
                json.put("baconPath", array);

            } else {
                query = "MATCH path = shortestPath((:actor{id:\"%s\"})-[*]-(:actor{id: \"nm0000102\"}))\n RETURN (nodes(path)) as path";
                query = String.format(query, actorId);
                Result result = this.session.run(query);

                while (result.hasNext()) {
                    Record str = result.next();
                    List<Object> nodes = str.get("path").asList();

                    for (Object node : nodes) {

                        String nodeString = node.toString();
                        int pos = nodeString.indexOf('>');
                        String Id = nodeString.substring(5, pos);
                        int i = Integer.parseInt(Id);
                        String query_actor = "MATCH (n) \n WHERE ID(n) = %d \n return n.id";
                        query_actor = String.format(query_actor, i);
                        Result res = this.session.run(query_actor);
                        while (res.hasNext()) {
                            Record record = res.next();
                            array.put(record.get("n.id").asString());
                            System.out.println(record.get("n.id").asString());
                        }


                    }
                    json.put("baconPath", array);


                    return json;

                }
            }
        }
        return json;

    }




}
