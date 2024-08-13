package net.ryanland.colossus.sys.file;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class LocalFile extends File {

    public LocalFile(@NotNull String pathname) {
        super(pathname);
    }

    public String getContent() {
        try {
            return new String(Files.readAllBytes(toPath()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void write(byte[] content) {
        delete();
        try {
            createNewFile();
            Files.write(toPath(), content, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String content) {
        write(content.getBytes());
    }

    public void write(JsonElement json) {
        write(json.toString());
    }

    public String getExtension() {
        return getPath().replaceFirst("^.*\\.", "");
    }

    @SuppressWarnings("unchecked")
    public <R extends JsonElement> R parseJson() {
        if (!getExtension().equals(LocalFileType.JSON.getExtension())) {
            throw new UnsupportedOperationException();
        }
        return (R) parseJson(getContent());
    }

    public static JsonElement parseJson(String json) {
        return JsonParser.parseString(json);
    }

    public static JsonObject jsonOfEntries(Map<String, Object> entries) {
        JsonObject json = new JsonObject();
        for (String key : entries.keySet()) {
            if (key.contains(".")) {
                key = key.replaceFirst("\\..+$", "");
                if (!json.has(key)) {
                    String finalKey = key;
                    // add a json object using recursion, with all members of the object as parameters (keys starting with "key.")
                    // dont use a stream to preserve the LinkedHashMap order
                    LinkedHashMap<String, Object> newEntries = new LinkedHashMap<>();
                    entries.forEach((eKey, eValue) -> {
                        if (eKey.startsWith(finalKey + ".")) {
                            newEntries.put(eKey.replaceFirst("^" + Matcher.quoteReplacement(finalKey) + "\\.", ""), eValue);
                        }
                    });
                    JsonObject obj = jsonOfEntries(newEntries);
                    json.add(key, obj);
                }
            } else {
                json.add(key, new GsonBuilder().create().toJsonTree(entries.get(key)));
            }
        }
        return json;
    }

    public static LinkedHashMap<String, Object> mapOfJson(JsonObject json) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        addJsonToMap(map, json, "");
        return map;
    }

    private static void addJsonToMap(LinkedHashMap<String, Object> map, JsonObject json, String keyPrefix) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue() instanceof JsonObject) {
                addJsonToMap(map, (JsonObject) entry.getValue(), keyPrefix + entry.getKey() + ".");
            } else {
                map.put(keyPrefix + entry.getKey(), new GsonBuilder().create().toJsonTree(entry.getValue()));
            }
        }
    }

    public static LocalFile validateDirectoryPath(String path) {
        path = path.replaceFirst("^/", "");
        LocalFile dir = new LocalFile(path);
        if (!dir.exists())
            throw new InvalidPathException(path, "This path is invalid or does not exist");
        if (!dir.isDirectory())
            throw new InvalidPathException(path, "The provided path is not a directory");
        return dir;
    }
}
