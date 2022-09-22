package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.events.command.SlashCommandEvent;
import net.ryanland.colossus.sys.entities.ColossusUser;

public class UserArgument extends SnowflakeArgument<User> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.USER);
    }

    @Override
    public ColossusUser resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return new ColossusUser(arg.getAsUser());
    }

    @Override
    public ColossusUser resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException {
        return new ColossusUser(Colossus.getJDA().retrieveUserById(id).complete());
    }
}
