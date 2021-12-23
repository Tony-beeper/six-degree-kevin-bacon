package ca.utoronto.utm.mcs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// TODO Please Write Your Tests For CI/CD In This Class. You will see
// these tests pass/fail on github under github actions.
public class AppTest {
    @BeforeAll
    static void beforeAllInit() throws IOException {
        App.main(null);
    }
    @BeforeEach
    void init() throws IOException, JSONException, InterruptedException {
        System.out.println("Initializing.....");
        JSONObject confirmReq1 = new JSONObject()
                .put("name", "James")
                .put("actorId", "0");
        sendRequest("/api/v1/addActor", "PUT", confirmReq1.toString());

        JSONObject confirmReq2 = new JSONObject()
                .put("name", "Kevin Bacon")
                .put("actorId", "nm0000102");
        sendRequest("/api/v1/addActor", "PUT", confirmReq2.toString());

        JSONObject confirmReq3 = new JSONObject()
                .put("name", "One Punch Man")
                .put("movieId", "1");
        sendRequest("/api/v1/addMovie", "PUT", confirmReq3.toString());

        JSONObject confirmReq4 = new JSONObject()
                .put("actorId", "nm0000102")
                .put("movieId", "1");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq4.toString());


        JSONObject confirmReq6 = new JSONObject()
                .put("actorId", "0")
                .put("movieId", "1");
        sendRequest("/api/v1/addRelationship", "PUT", confirmReq6.toString());

        JSONObject confirmReq7 = new JSONObject()
                .put("name", "Chris Evans")
                .put("actorId", "99");
        sendRequest("/api/v1/addActor", "PUT", confirmReq7.toString());
    }

    @AfterEach
    void cleanup() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq6 = new JSONObject()
                .put("clear", "clear");
        sendRequest("/api/v1/clearNodes", "DELETE", confirmReq6.toString());
    }

    final static String API_URL = "http://localhost:8080";
    public static HttpResponse<String> sendRequest(String endpoint, String method, String reqBody) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + endpoint))
                .method(method, HttpRequest.BodyPublishers.ofString(reqBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void addActorPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "Tony")
                .put("actorId", "1207");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());

    }

    @Test
    public void addActorFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "Tony");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addActor", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }

    @Test
    public void addMoviePass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "Iron Man")
                .put("movieId", "10");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }

    @Test
    public void addMovieFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("name", "Iron Man");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addMovie", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, confirmRes.statusCode());
    }
    @Test
    public void addRelationshipPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId", "99")
                .put("movieId", "1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void addRelationshipFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId", "nm1233333")
                .put("movieId", "-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/addRelationship", "PUT", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }
    @Test
    public void getActorPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId", "0");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getActor", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void getActorFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId", "-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getActor", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }
    @Test
    public void getMoviePass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("movieId", "1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getMovie", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void getMovieFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("movieId", "-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/getMovie", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }
    @Test
    public void hasRelationshipPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","0")
                .put("movieId", "1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/hasRelationship", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void hasRelationshipFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","-1")
                .put("movieId", "-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/hasRelationship", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }
    @Test
    public void computeBaconNumberPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","nm0000102");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconNumber", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void computeBaconNumberFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconNumber", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }
    @Test
    public void computeBaconPathPass() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","0");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconPath", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_OK, confirmRes.statusCode());
    }
    @Test
    public void computeBaconPathFail() throws JSONException, IOException, InterruptedException {
        JSONObject confirmReq = new JSONObject()
                .put("actorId","-1");
        HttpResponse<String> confirmRes = sendRequest("/api/v1/computeBaconPath", "GET", confirmReq.toString());
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, confirmRes.statusCode());
    }


}
