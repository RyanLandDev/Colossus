package net.ryanland.colossus;

public class Testing {

    public static void main(String[] args) {
        Colossus bot = new ColossusBuilder("src/config").build();
        bot.initialize();
    }
}
