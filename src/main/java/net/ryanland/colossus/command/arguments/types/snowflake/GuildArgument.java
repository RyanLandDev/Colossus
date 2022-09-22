package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.Guild;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.sys.entities.ColossusGuild;

public class GuildArgument extends PureSnowflakeArgument<Guild> {

    @Override
    public ColossusGuild resolve(CommandEvent event, String id) throws ArgumentException {
        Guild guild = Colossus.getJDA().getGuildById(id);
        if (guild == null) throw new NumberFormatException();
        return new ColossusGuild(guild);
    }
}
