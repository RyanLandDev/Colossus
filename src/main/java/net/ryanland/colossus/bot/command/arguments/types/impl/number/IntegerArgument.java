package net.ryanland.colossus.bot.command.arguments.types.impl.number;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.bot.command.arguments.types.NumberArgument;
import net.ryanland.colossus.bot.events.CommandEvent;

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
    public OptionType getType() {
        return OptionType.INTEGER;
    }

    @Override
    public Integer parsed(OptionMapping argument, CommandEvent event) throws ArgumentException {
        Integer parsed = Integer.parseInt(argument.getAsString());
        if (parsed >= min && parsed <= max) {
            return parsed;
        } else {
            throw new MalformedArgumentException("Number must be in between " + min + " and " + max + ".");
        }
    }
}
