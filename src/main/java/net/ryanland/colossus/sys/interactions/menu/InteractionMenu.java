package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.interactions.Interaction;
import net.ryanland.colossus.bot.command.CommandException;

public interface InteractionMenu {

    void send(Interaction interaction) throws CommandException;
}
