package dev.ryanland.colossus.sys.util;

import com.google.gson.JsonObject;
import dev.ryanland.colossus.sys.file.LocalFile;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WebUtil {

    private static final OkHttpClient client = new OkHttpClient();

    @NotNull
    public static CompletableFuture<Response> request(Request request) {
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
     * Initiates a GET request to the URL provided and returns the result as a Response
     */
    public static CompletableFuture<Response> get(String url) {
        Request request = new Request.Builder().url(url).build();
        return request(request);
    }

    /**
     * Initiates a GET request to the URL provided and returns the result as a JsonObject
     * @throws com.google.gson.JsonParseException if the response is not valid JSON
     */
    public static CompletableFuture<JsonObject> getJson(String url) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        CompletableFuture<Response> request = get(url);
        request.thenAccept(response -> {
            try {
                future.complete(LocalFile.parseJson(response.body().string()).getAsJsonObject());
            } catch (IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    /**
     * Initiates a POST request to the URL provided and returns the result as a Response
     */
    public static CompletableFuture<Response> post(String url, RequestBody body) {
        Request request = new Request.Builder().url(url).post(body).build();
        return request(request);
    }

    /**
     * Initiates a POST request to the URL provided and returns the result as a JsonObject
     * @throws com.google.gson.JsonParseException if the response is not valid JSON
     */
    public static CompletableFuture<JsonObject> postJson(String url, RequestBody body) {
        CompletableFuture<JsonObject> future = new CompletableFuture<>();
        CompletableFuture<Response> request = post(url, body);
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
