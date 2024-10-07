package dev.ryanland.colossus.command.arguments.types.snowflake;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class RoleArgument extends SnowflakeArgument<Role> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.ROLE);
    }

    @Override
    public Role resolveSlashCommandArg(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return arg.getAsRole();
    }

    @Override
    public Role resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException {
        return event.getGuild().getRoleById(id);
    }
}
