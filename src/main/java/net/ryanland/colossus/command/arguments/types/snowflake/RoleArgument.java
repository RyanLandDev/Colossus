package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class RoleArgument extends SnowflakeArgument<Role> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.ROLE);
    }

    @Override
    public Role resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return arg.getAsRole();
    }

    @Override
    public Role resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException {
        return event.getGuild().getRoleById(id);
    }
}
