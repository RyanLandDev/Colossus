package net.ryanland.colossus.command.arguments.types.primitive;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentOptionData;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

import java.util.Deque;

public class EndlessStringArgument extends Argument<String> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.STRING);
    }

    @Override
    public String resolveSlashCommandArgument(Deque<OptionMapping> args, SlashEvent event) throws ArgumentException {
        return args.pop().getAsString();
    }

    @Override
    public String resolveMessageCommandArgument(Deque<String> args, MessageCommandEvent event) throws ArgumentException {
        String result = String.join(" ", args);
        args.clear();
        return result;
    }
}
