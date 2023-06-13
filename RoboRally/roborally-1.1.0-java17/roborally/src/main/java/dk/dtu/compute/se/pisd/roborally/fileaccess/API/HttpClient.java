package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient {
    private String serverUrl = "http://localhost:8080/";
    private CloseableHttpClient httpClient;

    public HttpClient() {

        this.httpClient = HttpClients.createDefault();
    }



    public String sendPostRequest(String path, String jsonInput) throws Exception {
        HttpPost post = new HttpPost(serverUrl + path);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(jsonInput));

        HttpResponse response = httpClient.execute(post);

        return EntityUtils.toString(response.getEntity());
    }

    public String sendGetRequest(String path) throws Exception {
        HttpGet get = new HttpGet(serverUrl + path);
        get.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(get);

        return EntityUtils.toString(response.getEntity());
    }
}


/*import java.time.Duration;

import static dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard.saveBoard;

public class HttpClient {

    private static final java.net.http.HttpClient httpClient = java.net.http.HttpClient.newBuilder()
            .version(java.net.http.HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public void saveGame() {
        // Gather all the necessary data
        String boardData = gatherBoardData();

        // Convert the data into a JSON or XML string
        String jsonString = convertToJSON(boardData);

        // Send the data to the server
        sendToServer("/saveGame", jsonString);
    }

    @POST
    @Path("/saveGame")
    public void saveGame(String jsonString) {
        // Parse the JSON string back into a Board object
        Board board = parseJSON(jsonString);

        // Save the board
        saveBoard(board);
    }

    @GET

}
*/
