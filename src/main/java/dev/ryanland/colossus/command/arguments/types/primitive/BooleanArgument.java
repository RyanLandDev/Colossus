package dev.ryanland.colossus.command.arguments.types.primitive;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import dev.ryanland.colossus.command.arguments.ArgumentOptionData;
import dev.ryanland.colossus.command.arguments.types.SingleArgument;
import dev.ryanland.colossus.events.command.MessageCommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;

public class BooleanArgument extends SingleArgument<Boolean> {

    @Override
    public ArgumentOptionData getArgumentOptionData() {
        return new ArgumentOptionData(OptionType.BOOLEAN);
    }

    @Override
    public Boolean resolveSlashCommandArgument(OptionMapping arg, SlashCommandEvent event) throws ArgumentException {
        return arg.getAsBoolean();
    }

    @Override
    public Boolean resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return Boolean.parseBoolean(arg);
    }
}
