package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Equivalent of {@link StringArgument}, except if a message command is used,
 */
public class QuoteStringArgument extends Argument<String> {

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public String resolveSlashCommandArgument(Deque<OptionMapping> args, SlashEvent event) throws ArgumentException {
        return args.pop().getAsString();
    }

    @Override
    public String resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        String ERROR_MESSAGE = "Invalid quote, must start and end with `'` or `\"`.";

        // If the argument does not begin with a quote, throw an error
        if (!(args.element().startsWith("'") || args.peek().startsWith("\"")))
            throw new MalformedArgumentException(ERROR_MESSAGE);

        // Repeat over all arguments until one ends with a quote
        List<String> elements = new ArrayList<>();
        String argument = args.poll();
        elements.add(argument);
        while (!(argument.endsWith("'") || argument.endsWith("\""))) {
            argument = args.poll();
            if (argument == null)
                throw new MalformedArgumentException(ERROR_MESSAGE);
            elements.add(argument);
        }

        // Join elements and remove quotes
        String result = String.join(" ", elements);
        return result.substring(1, result.length()-1);
    }
}
