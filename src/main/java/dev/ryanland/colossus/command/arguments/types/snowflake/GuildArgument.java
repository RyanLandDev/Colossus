package dev.ryanland.colossus.command.arguments.types.snowflake;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.dv8tion.jda.api.entities.Guild;
import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusGuild;

public class GuildArgument extends PureSnowflakeArgument<Guild> {

    @Override
    public ColossusGuild resolve(CommandEvent event, String id) throws ArgumentException {
        Guild guild = Colossus.getJDA().getGuildById(id);
        if (guild == null) throw new NumberFormatException();
        return new ColossusGuild(guild);
    }
}
