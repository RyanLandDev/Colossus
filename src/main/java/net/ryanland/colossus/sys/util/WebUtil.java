package net.ryanland.colossus.sys.util;

import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.LocalFile;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class WebUtil {

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Initiates a GET request to the URL provided and returns the result as a Response
     */
    public static CompletableFuture<Response> request(String url) {
        Request request = new Request.Builder().url(url).build();
        CompletableFuture<Response> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                future.complete(response);
            }
        });
        return future;
    }

    /**
     * Initiates a GET request to the URL provided and returns the result as an JsonObject
     * @throws com.google.gson.JsonParseException if the response is not valid JSON
     */
    public static CompletableFuture<JsonObject> requestJson(String url) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        CompletableFuture<Response> request = request(url);
        request.thenAccept(response -> {
            try {
                future.complete(LocalFile.parseJson(response.body().string()).getAsJsonObject());
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
