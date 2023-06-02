package net.ryanland.colossus.sys.file.database.mongo;

import net.ryanland.colossus.sys.file.database.ValueProvider;
import org.bson.Document;

public interface MongoValueProvider<T> extends ValueProvider<Object, Document, T> {
}
