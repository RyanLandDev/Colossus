package net.ryanland.colossus.util.message.interactions.menu;

import net.dv8tion.jda.api.interactions.Interaction;
import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;

public interface InteractionMenu {

    void send(Interaction interaction) throws CommandException;
}
