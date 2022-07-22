package net.ryanland.colossus.command.arguments;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArgumentSet extends LinkedHashMap<String, Argument<?>> {

    public ArgumentSet addArguments(Argument<?>... arguments) {
        putAll(Arrays.stream(arguments).collect(Collectors.toMap(Argument::getName, Function.identity())));
        return this;
    }
}
