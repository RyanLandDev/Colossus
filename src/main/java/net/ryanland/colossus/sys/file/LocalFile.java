package net.ryanland.colossus.sys.file;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardOpenOption;

public class LocalFile extends File {

    public LocalFile(@NotNull String pathname) {
        super(pathname);
    }

    public String getContent() throws IOException {
        return new String(Files.readAllBytes(toPath()));
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

    public void write(JsonObject json) {
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
