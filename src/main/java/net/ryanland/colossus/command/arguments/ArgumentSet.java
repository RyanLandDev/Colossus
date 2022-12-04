package net.ryanland.colossus.command.arguments;

import java.util.LinkedHashMap;

public class ArgumentSet extends LinkedHashMap<String, Argument<?>> {

    public ArgumentSet addArguments(Argument<?>... arguments) {
        for (Argument<?> argument : arguments) {
            put(argument.getName(), argument);//don't use streams to retain order
        }
        return this;
    }
}
