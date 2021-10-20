package net.ryanland.colossus.command.arguments.types.impl.number;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.types.NumberArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

public class DoubleArgument extends NumberArgument<Double> {

    @Override
    public Double parsed(OptionMapping argument, MessageCommandEvent event) throws ArgumentException {
        return Double.parseDouble(argument.getAsString());
    }
}
