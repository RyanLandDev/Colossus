package net.ryanland.colossus.command;

import net.ryanland.colossus.events.SlashCommandEvent;

public interface SlashCommand {

    void run(SlashCommandEvent event) throws CommandException;
}
