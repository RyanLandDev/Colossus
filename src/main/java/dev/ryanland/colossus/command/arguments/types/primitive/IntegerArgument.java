package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class IntegerArgument extends NumberArgument<Integer> {

    private Integer min = null;
    private Integer max = null;

    public IntegerArgument() {
    }

    public IntegerArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntegerArgument setMinimum(int min) {
        this.min = min;
        return this;
    }

    public IntegerArgument setMaximum(int max) {
        this.max = max;
        return this;
    }

    private Integer check(Integer value) throws ArgumentException {
        if (value >= min && value <= max)
            return value;
        else
            throw new MalformedArgumentException("Number must be in between " + min + " and " + max + ".");
    }

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        ArgumentOptionData data = new ArgumentOptionData(OptionType.INTEGER);
        if (min != null) data.setMinValue(min);
        if (max != null) data.setMaxValue(max);
        return data;
    }

    @Override
    public Integer resolveSlashCommandArg(SlashCommandEvent event, OptionMapping arg) throws ArgumentException {
        return arg.getAsInt();
    }

    @Override
    public Integer resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return check(Integer.parseInt(arg));
    }
}
