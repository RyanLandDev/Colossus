package net.ryanland.colossus.bot.command.arguments.types.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.bot.command.arguments.types.SingleArgument;
import net.ryanland.colossus.bot.events.CommandEvent;

public class GuildArgument extends SingleArgument<Guild> {

    @Override
    public Guild parsed(OptionMapping argument, CommandEvent event) throws ArgumentException {
        try {
            Guild guild = Colossus.getJda().getGuildById(argument.getAsString());
            if (guild != null) return guild;
        } catch (NumberFormatException ignored) {

        }

        throw new MalformedArgumentException("Invalid guild provided. Must be a valid ID.");
    }
}
