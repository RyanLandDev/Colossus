package net.ryanland.colossus.bot.command.arguments.types.impl.number;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.types.NumberArgument;
import net.ryanland.colossus.bot.events.CommandEvent;

public class FloatArgument extends NumberArgument<Float> {

    @Override
    public Float parsed(OptionMapping argument, CommandEvent event) {
        return Float.parseFloat(argument.getAsString());
    }
}
