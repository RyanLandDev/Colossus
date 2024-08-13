package dev.ryanland.colossus.command.arguments.types.snowflake;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

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
