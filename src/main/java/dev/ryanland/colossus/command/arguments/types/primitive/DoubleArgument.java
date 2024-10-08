package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class DoubleArgument extends NumberArgument<Double> {

    private Double min = null;
    private Double max = null;

    public DoubleArgument() {
    }

    public DoubleArgument(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public DoubleArgument setMinimum(double min) {
        this.min = min;
        return this;
    }

    public DoubleArgument setMaximum(double max) {
        this.max = max;
        return this;
    }

    private Double check(Double value) throws ArgumentException {
        if (value >= min && value <= max)
            return value;
        else
            throw new MalformedArgumentException("Number must be in between " + min + " and " + max + ".");
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        ArgumentOptionData data = new ArgumentOptionData(OptionType.NUMBER);
        if (min != null) data.setMinValue(min);
        if (max != null) data.setMaxValue(max);
        return data;
    }
    
    @Override
    public Double resolveSlashCommandArg(SlashCommandEvent event, OptionMapping arg) throws ArgumentException {
        return arg.getAsDouble();
    }

    @Override
    public Double resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return check(Double.parseDouble(arg));
    }
}
