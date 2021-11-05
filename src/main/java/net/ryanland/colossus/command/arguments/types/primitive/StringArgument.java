package net.ryanland.colossus.command.arguments.types.primitive;

import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.CommandEvent;

public class StringArgument extends ArgumentStringResolver<String> {

    @Override
    public String resolve(String arg, CommandEvent event) throws ArgumentException {
        return arg;
    }
}
