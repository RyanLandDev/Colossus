package net.ryanland.colossus.sys.file;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class LocalFile extends File {

    public LocalFile(@NotNull String pathname) {
        super(pathname);
    }

    public String getContent() throws IOException {
        return new String(Files.readAllBytes(toPath()));
    }

    public void write(byte[] content) throws IOException {
        delete();
        createNewFile();
        Files.write(toPath(), content, StandardOpenOption.WRITE);
    }

    public void write(String content) throws IOException {
        write(content.getBytes());
    }

    public void write(JsonObject json) throws IOException {
        write(json.toString());
    }

    public String getExtension() {
        return getPath().replaceFirst("^.*\\.", "");
    }

    public JsonObject parseJson() throws IOException {
        if (!getExtension().equals(LocalFileType.JSON.getExtension())) {
            throw new UnsupportedOperationException();
        }
        return JsonParser.parseString(getContent()).getAsJsonObject();
    }

    public static JsonObject jsonOfKeys(String... keys) {
        JsonObject json = new JsonObject();
        for (String key : keys) json.addProperty(key, "");
        return json;
    }
}
