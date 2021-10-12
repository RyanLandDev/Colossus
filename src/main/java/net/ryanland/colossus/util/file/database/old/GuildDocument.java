package net.ryanland.colossus.util.file.database.old;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

public class GuildDocument extends BaseDocument implements SnowflakeDocument {

    public GuildDocument(Document document) {
        super(document);
    }

    @Override
    public void updated(List<Bson> updates) {
        performUpdate(DocumentCache.GUILD_COLLECTION, Filters.eq("id", getId()), updates);
    }

    @Override
    public void cache() {
        DocumentCache.GUILD_CACHE.put(getId(), this);
    }

    // ------------------------------------------------------

    @Override
    public String getId() {
        return getString("id");
    }

}
