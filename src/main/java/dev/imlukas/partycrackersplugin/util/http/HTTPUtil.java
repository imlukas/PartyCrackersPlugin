package dev.imlukas.partycrackersplugin.util.http;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPUtil {


    public static HttpResponse<String> makeRequest(String url, String bearer, HTTPMethod method, JsonObject object) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "application/json",
                        "Authorization", "Bearer " + bearer)
                .method(method.name(), HttpRequest.BodyPublishers.ofString(object.toString()))
                .build();

        HttpResponse<String> response;
        response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static HttpResponse<String> makePostRequest(String url, String bearer, JsonObject object) throws IOException, InterruptedException {
        return makeRequest(url, bearer, HTTPMethod.POST, object);
    }

    public enum HTTPMethod {
        GET,
        POST,
        PUT,
        DELETE
    }
}
