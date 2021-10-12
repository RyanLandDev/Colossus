package net.ryanland.colossus.util.file.database.old;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import net.ryanland.colossus.Colossus;

public class MongoDB {

    public static String DATABASE_SECRET;
    public static MongoClient MONGO_CLIENT;
    public static MongoDatabase DATABASE;

    static {
        DATABASE_SECRET = Colossus.getConfig().getDatabaseURI();
        MONGO_CLIENT = MongoClients.create(DATABASE_SECRET);
        DATABASE = MONGO_CLIENT.getDatabase("EmpireBot");
    }
}
