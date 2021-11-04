package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.Command;

public interface SlashCommandArgumentChoices {

    Command.Choice[] getChoices();
}
