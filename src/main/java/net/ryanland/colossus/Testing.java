package net.ryanland.colossus;

import net.ryanland.colossus.bot.command.CommandExecutionType;

public class Testing {

    public static void main(String[] args) {
        Colossus bot = new ColossusBuilder("src/config", CommandExecutionType.CONTENT).build();
        bot.initialize();
    }
}
