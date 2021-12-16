package net.ryanland.colossus;

import net.ryanland.colossus.sys.file.JsonDatabaseDriver;

public class Testing {

    public static void main(String[] args) {
        Colossus bot = new ColossusBuilder("src/config")
            .setDatabaseDriver(new JsonDatabaseDriver("src/database"))
            .build();
        bot.initialize();
    }
}
