package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class DoubleArgument extends NumberArgument<Double> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public Double resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return Double.parseDouble(arg.getAsString());
    }

    @Override
    public Double resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return Double.parseDouble(arg);
    }
}
