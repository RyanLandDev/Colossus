package net.ryanland.colossus.sys.file.database.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ryanland.colossus.sys.file.database.ValueProvider;

public interface JsonValueProvider<T> extends ValueProvider<JsonElement, JsonObject, T> {
}
