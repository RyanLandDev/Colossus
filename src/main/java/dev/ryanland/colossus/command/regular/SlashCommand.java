package dev.ryanland.colossus.command.regular;

import dev.ryanland.colossus.command.CommandException;
import dev.ryanland.colossus.events.command.SlashCommandEvent;

public interface SlashCommand {

    void run(SlashCommandEvent event) throws CommandException;
}
