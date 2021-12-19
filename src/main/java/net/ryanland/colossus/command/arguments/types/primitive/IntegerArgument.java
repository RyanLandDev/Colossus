package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class IntegerArgument extends NumberArgument<Integer> {

    private Integer min = Integer.MIN_VALUE;
    private Integer max = Integer.MAX_VALUE;

    public IntegerArgument() {
    }

    public IntegerArgument(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntegerArgument min(int min) {
        this.min = min;
        return this;
    }

    public IntegerArgument max(int max) {
        this.max = max;
        return this;
    }

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.INTEGER;
    }

    @Override
    public Integer resolveSlashCommandArgument(SlashEvent event, OptionMapping arg) throws ArgumentException {
        return check((int) arg.getAsLong());
    }

    @Override
    public Integer resolveMessageCommandArgument(MessageCommandEvent event, String arg) throws ArgumentException {
        return check(Integer.parseInt(arg));
    }

    private Integer check(Integer value) throws ArgumentException {
        if (value >= min && value <= max)
            return value;
        else
            throw new MalformedArgumentException("Number must be in between " + min + " and " + max + ".");
    }
}
