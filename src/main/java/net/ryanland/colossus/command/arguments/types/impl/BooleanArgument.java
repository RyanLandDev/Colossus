package net.ryanland.colossus.command.arguments.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.ContentCommandEvent;

public class BooleanArgument extends SingleArgument<Boolean> {

    @Override
    public OptionType getType() {
        return OptionType.BOOLEAN;
    }

    @Override
    public Boolean parsed(OptionMapping argument, ContentCommandEvent event) throws ArgumentException {
        return argument.getAsBoolean();
    }
}
