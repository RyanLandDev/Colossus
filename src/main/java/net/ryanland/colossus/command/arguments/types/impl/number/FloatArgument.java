package net.ryanland.colossus.command.arguments.types.impl.number;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.types.NumberArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

public class FloatArgument extends NumberArgument<Float> {

    @Override
    public Float parsed(OptionMapping argument, MessageCommandEvent event) {
        return Float.parseFloat(argument.getAsString());
    }
}
