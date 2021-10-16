package net.ryanland.colossus.command.arguments;

import java.util.ArrayList;
import java.util.Arrays;

public class ArgumentSet extends ArrayList<Argument<?>> {

    public ArgumentSet addArguments(Argument<?>... arguments) {
        addAll(Arrays.asList(arguments));
        return this;
    }
}
