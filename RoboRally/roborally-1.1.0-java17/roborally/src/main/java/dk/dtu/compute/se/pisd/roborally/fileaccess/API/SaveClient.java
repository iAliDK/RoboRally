package dk.dtu.compute.se.pisd.roborally.fileaccess.API;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class SaveClient
{
    private String serverUrl = "http://localhost:8080/";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public boolean sendPostRequest(String path, String jsonInput) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .uri(URI.create(serverUrl + path))
                    .setHeader("Content-Type", "application/json")
                    .build();
            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            return result.equals("added")? true : false;
        } catch (Exception e) {
            return false;
        }

    }
    public String sendGetRequest(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(serverUrl + path))
                .setHeader("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
        return result;
    }

    public boolean sendPutRequest(String path, String jsonInput) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(jsonInput))
                .uri(URI.create(serverUrl + path))
                .setHeader("Content-Type", "application/json")
                .build();
        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
        return result.equals("updated")? true : false;
    }

}
