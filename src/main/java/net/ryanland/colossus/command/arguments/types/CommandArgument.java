package net.ryanland.colossus.command.arguments.types;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.executor.CommandHandler;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public class CommandArgument extends SingleArgument<Command> {

    private Command findCommand(String name) throws ArgumentException {
        Command command = CommandHandler.getCommand(name);
        if (command == null)
            throw new MalformedArgumentException("This command was not found.");
        else
            return command;
    }

    @Override
    public OptionType getSlashCommandOptionType() {
        return OptionType.STRING;
    }

    @Override
    public Command resolveSlashCommandArgument(OptionMapping arg, SlashEvent event) throws ArgumentException {
        return findCommand(arg.getAsString());
    }

    @Override
    public Command resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        return findCommand(arg);
    }
}
