package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class FloatArgument extends NumberArgument<Float> {

    private Float min = null;
    private Float max = null;

    public FloatArgument() {
    }

    public FloatArgument(float min, float max) {
        this.min = min;
        this.max = max;
    }

    public FloatArgument setMinimum(float min) {
        this.min = min;
        return this;
    }

    public FloatArgument setMaximum(float max) {
        this.max = max;
        return this;
    }

    private Float check(Float value) throws ArgumentException {
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
    public Float resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return (float) arg.getAsDouble();
    }

    @Override
    public Float resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return check(Float.parseFloat(arg));
    }
}
