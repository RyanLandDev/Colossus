package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.events.command.CommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class StringArgument extends ArgumentStringResolver<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        ArgumentOptionData data = new ArgumentOptionData(OptionType.STRING);
        if (min != null) data.setMinLength(min);
        if (max != null) data.setMaxLength(max);
        return data;
    }

    private Integer min = 1;
    private Integer max = 4000;

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
