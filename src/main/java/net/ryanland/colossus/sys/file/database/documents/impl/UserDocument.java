package net.ryanland.colossus.sys.file.database.documents.impl;

import com.mongodb.client.model.Filters;
import net.ryanland.colossus.sys.file.database.DocumentCache;
import net.ryanland.colossus.sys.file.database.documents.BaseDocument;
import net.ryanland.colossus.sys.file.database.documents.SnowflakeDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public class UserDocument extends BaseDocument implements SnowflakeDocument {

    // Defaults here...

    // ------

    public UserDocument(Document document) {
        super(document);
    }

    // Private vars here....
    // --------

    @Override
    public void updated(List<Bson> updates) {
        // Update checks here...
        // -----

        performUpdate(DocumentCache.USER_COLLECTION, Filters.eq("id", getId()), updates);
    }

    @Override
    public void cache() {
        DocumentCache.USER_CACHE.put(getId(), this);
    }

    // Setters here...
    // -----

    @Override
    public String getId() {
        return getString("id");
    }

    // Getters here...
    // ------

}
