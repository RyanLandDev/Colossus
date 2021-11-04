package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.entities.Guild;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.CommandEvent;

public class GuildArgument extends ArgumentStringResolver<Guild> {

    @Override
    public Guild resolve(String arg, CommandEvent event) throws ArgumentException {
        try {
            Guild guild = Colossus.getJda().getGuildById(arg);
            if (guild != null)
                return guild;
        } catch (NumberFormatException ignored) {
        }

        throw new MalformedArgumentException(
            "Invalid guild provided. Must be a valid ID. The bot must also be in the specified guild.");
    }
}
