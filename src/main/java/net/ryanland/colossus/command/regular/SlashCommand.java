package net.ryanland.colossus.command.regular;

import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.command.SlashCommandEvent;

public interface SlashCommand {

    void run(SlashCommandEvent event) throws CommandException;
}
