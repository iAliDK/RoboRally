package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * The SaveClient class is responsible for sending all the requests to the server.
 */
public class SaveClient {
    private final String serverUrl = "http://localhost:8080/";

    /**
     * Calling the newBuilder method of the HttpClient class, which lets you configure the http client instance before building it.
     * I am defining it's protocol as the HTTP/2 protocol.
     * I am giving it a timeout time of 10 seconds.
     */
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * The sendPostRequest method attempts to send the JSON file as a string to the path given asynchronously.
     * It builds the HttpRequest, sets the body as a string containing jsonInput, the uri as the serverUrl + path given.
     * The header sets the content type as JSON. It waits for the response string for 5 seconds and returns true if the response is "added".
     *
     * @param path      the path on the server.
     * @param jsonInput the JSON file as a string.
     * @return Returns true if it manages to send asynchronously and false if not.
     * @throws Exception returns false if the result string isn't "added"
     * @author Qiao.
     */
    public boolean sendPostRequest(String path, String jsonInput) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .uri(URI.create(serverUrl + path))
                    .setHeader("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added");
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * The sendGetRequest requests a JSON string from the server using at the path given with the parameter.
     * If the response times out, or it doesn't exist on the path it returns null.
     *
     * @param path the path on the server.
     * @return Returns the JSON string of the save or null if it can't.
     * @throws Exception returns null if the result string isn't the JSON string.
     * @author Qiao.
     */
    public String sendGetRequest(String path) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(serverUrl + path))
                    .setHeader("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * The sendPutRequest method attempts to send the JSON file as a string to the path given asynchronously.
     * It builds the HttpRequest, sets the body as a string containing jsonInput, the uri as the serverUrl + path given.
     * The header sets the content type as JSON. It waits for the response string for 5 seconds and returns true if the response is "added".
     *
     * @param path      the path on the server.
     * @param jsonInput the JSON file as a string.
     * @return Returns true if it manages to update the JSON file.
     * @throws Exception returns false if the result string isn't "updated"
     * @author Qiao.
     */
    public boolean sendPutRequest(String path, String jsonInput) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .uri(URI.create(serverUrl + path))
                    .setHeader("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("updated");
        } catch (Exception e) {
            return false;
        }
    }
}
