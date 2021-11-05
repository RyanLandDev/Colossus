package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class FloatArgument extends NumberArgument<Float> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public Float resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return Float.parseFloat(arg.getAsString());
    }

    @Override
    public Float resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return Float.parseFloat(arg);
    }
}
