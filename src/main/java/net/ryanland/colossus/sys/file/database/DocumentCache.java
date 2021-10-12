package net.ryanland.colossus.sys.file.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.sys.file.database.documents.BaseDocument;
import net.ryanland.colossus.sys.file.database.documents.SnowflakeDocument;
import net.ryanland.colossus.sys.file.database.documents.impl.GlobalDocument;
import net.ryanland.colossus.sys.file.database.documents.impl.GuildDocument;
import net.ryanland.colossus.sys.file.database.documents.impl.UserDocument;
import org.bson.Document;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class DocumentCache {

    // Collections
    public static final MongoCollection<Document> GLOBAL_COLLECTION = MongoDB.DATABASE.getCollection("global");
    public static final MongoCollection<Document> GUILD_COLLECTION = MongoDB.DATABASE.getCollection("guilds");
    public static final MongoCollection<Document> USER_COLLECTION = MongoDB.DATABASE.getCollection("users");

    // Caches
    public static volatile GlobalDocument GLOBAL_CACHE;
    public static volatile WeakHashMap<String, GuildDocument> GUILD_CACHE = new WeakHashMap<>();
    public static volatile WeakHashMap<String, UserDocument> USER_CACHE = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public static <R, T extends BaseDocument> R getCache(Class<T> type) {
        if (type.isAssignableFrom(GlobalDocument.class)) {
            return (R) GLOBAL_CACHE;
        }
        if (type.isAssignableFrom(GuildDocument.class)) {
            return (R) GUILD_CACHE;
        }
        if (type.isAssignableFrom(UserDocument.class)) {
            return (R) USER_CACHE;
        }
        throw new UnsupportedOperationException();
    }

    public static <T extends BaseDocument> MongoCollection<Document> getCollection(Class<T> type) {
        if (type.isAssignableFrom(GlobalDocument.class)) {
            return GLOBAL_COLLECTION;
        }
        if (type.isAssignableFrom(GuildDocument.class)) {
            return GUILD_COLLECTION;
        }
        if (type.isAssignableFrom(UserDocument.class)) {
            return USER_COLLECTION;
        }
        throw new UnsupportedOperationException();
    }

    // --------------------------------------------

    public static GlobalDocument getGlobal() {
        return nullOr(GLOBAL_CACHE, DocumentCache::retrieveGlobal);
    }

    public static GlobalDocument retrieveGlobal() {
        return nullOr(findGlobal(), DocumentCache::insertGlobal);
    }

    public static GlobalDocument findGlobal() {
        Document document = GLOBAL_COLLECTION.find().first();
        if (document == null) return null;

        return new GlobalDocument(document);
    }

    public static GlobalDocument insertGlobal() {
        Document document = new Document();
        GLOBAL_COLLECTION.insertOne(document);

        return new GlobalDocument(document);
    }

    public static <R extends BaseDocument & SnowflakeDocument, T extends ISnowflake> R get(T client, Class<R> type) {
        return get(client, type, false);
    }

    public static <R extends BaseDocument & SnowflakeDocument, T extends ISnowflake> R get(T client, Class<R> type, boolean nullIfMissing) {
        return get(client.getId(), type, nullIfMissing);
    }

    public static <T extends BaseDocument & SnowflakeDocument> T get(String id, Class<T> type, boolean nullIfMissing) {
        return nullOr(getFromCache(id, type), () -> retrieve(id, type, nullIfMissing));
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseDocument & SnowflakeDocument> T getFromCache(String id, Class<T> type) {
        return ((WeakHashMap<String, T>) getCache(type)).get(id);
    }

    public static <T extends BaseDocument & SnowflakeDocument> T retrieve(String id, Class<T> type, boolean nullIfMissing) {
        if (nullIfMissing) {
            return find(id, type);
        } else {
            return nullOr(find(id, type), () -> insert(id, type));
        }
    }

    public static <T extends BaseDocument & SnowflakeDocument> T find(String id, Class<T> type) {
        Document document = getCollection(type).find(Filters.eq("id", id)).first();
        if (document == null) return null;

        cache(document, type);
        return castDocument(document, type);
    }

    public static <T extends BaseDocument & SnowflakeDocument> T insert(String id, Class<T> type) {
        Document document = new Document("id", id);
        getCollection(type).insertOne(document);

        cache(document, type);
        return castDocument(document, type);
    }

    public static <T extends BaseDocument & SnowflakeDocument> void delete(String id, Class<T> type) {
        getCollection(type).deleteOne(Filters.eq("id", id));
        removeFromCache(id, type);
    }

    public static <T extends BaseDocument & SnowflakeDocument, C extends ISnowflake> void update(C client, Class<T> type) {
        update(client.getId(), type);
    }

    public static <T extends BaseDocument & SnowflakeDocument> void update(String id, Class<T> type) {
        get(id, type, false).update();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseDocument> T castDocument(Document document, Class<T> type) {
        if (document == null) return null;

        if (type.isAssignableFrom(GlobalDocument.class)) {
            return (T) new GlobalDocument(document);
        }
        if (type.isAssignableFrom(GuildDocument.class)) {
            return (T) new GuildDocument(document);
        }
        if (type.isAssignableFrom(UserDocument.class)) {
            return (T) new UserDocument(document);
        }

        throw new UnsupportedOperationException();
    }

    public static <T extends BaseDocument> void cache(Document document, Class<T> type) {
        castDocument(document, type).cache();
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseDocument & SnowflakeDocument> void removeFromCache(String id, Class<T> type) {
        ((WeakHashMap<String, T>) getCache(type)).remove(id);
    }

    /**
     * Alternative to {@link Objects#requireNonNullElse(Object, Object)} with {@link Supplier}
     */
    public static <T> T nullOr(T object, Supplier<T> compareTo) {
        return object != null ? object : compareTo.get();
    }

}
