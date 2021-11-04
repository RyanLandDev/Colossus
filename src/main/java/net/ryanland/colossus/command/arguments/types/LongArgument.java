package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class LongArgument extends NumberArgument<Long> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.INTEGER;
    }

    @Override
    public Long resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return arg.getAsLong();
    }

    @Override
    public Long resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return Long.parseLong(arg);
    }
}
