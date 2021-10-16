package net.ryanland.colossus.command;

import net.ryanland.colossus.events.SlashEvent;

public interface SlashCommand {

    void run(SlashEvent event) throws CommandException;
}
