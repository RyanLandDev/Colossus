package net.ryanland.colossus.sys.util;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.LocalFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebUtil {

    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Makes a GET request to the URL provided and returns the result as a String<br>
     * Note: This method will block the thread until the response is received
     */
    public static String request(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(url)).GET().build();
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Bad request with URL " + url);
        }
    }

    /**
     * Makes a GET request to the URL provided and returns the result as an JsonObject<br>
     * Note: This method will block the thread until the response is received
     * @throws com.google.gson.JsonParseException if the response is not valid JSON
     */
    public static JsonObject requestJson(String url) {
        return LocalFile.parseJson(request(url));
    }
}
