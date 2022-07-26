package net.ryanland.colossus.command.arguments.types.primitive;

import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.CommandEvent;

public class StringArgument extends ArgumentStringResolver<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return (ArgumentOptionData) super.getArgumentOptionData().setRequiredRange(min, max);
    }

    private int min;
    private int max;

    public StringArgument() {
    }

    public StringArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public StringArgument setMinimum(int min) {
        this.min = min;
        return this;
    }

    public StringArgument setMaximum(int max) {
        this.max = max;
        return this;
    }

    private String check(String value) throws ArgumentException {
        if (value.length() >= min && value.length() <= max)
            return value;
        else
            throw new MalformedArgumentException("Argument must be longer than " + min +
                " characters and shorter than " + max + " characters.");
    }

    @Override
    public String resolve(String arg, CommandEvent event) throws ArgumentException {
        return check(arg);
    }
}
