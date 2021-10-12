package net.ryanland.colossus.bot.command.executor;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.bot.command.executor.checks.CommandCheck;
import net.ryanland.colossus.bot.command.executor.checks.CommandCheckException;
import net.ryanland.colossus.bot.command.executor.exceptions.CommandException;
import net.ryanland.colossus.bot.command.executor.finalizers.CommandFinalizer;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.builders.PresetBuilder;
import net.ryanland.colossus.sys.message.builders.PresetType;

import java.util.List;

public class CommandExecutor {

    public void run(CommandEvent event) {
        List<OptionMapping> args = event.getOptions();

        Command command = CommandHandler.getCommand(event.getName());
        if (command == null) return;
        event.setCommand(command);

        execute(event, args);
    }

    public void execute(CommandEvent event, List<OptionMapping> args) {
        Command command = event.getCommand();

        if (event.getSubCommandGroup() != null) {
            command = command.getInfo().getSubCommandGroupMap().get(event.getSubCommandGroup()).getSubCommand(event.getSubCommandName());
        } else if (event.getSubCommandName() != null) {
            command = command.getInfo().getSubCommandMap().get(event.getSubCommandName());
        }

        event.setCommand(command);

        try {
            for (CommandCheck check : CommandCheck.getChecks()) {
                if (check.check(event)) {
                    event.performReply(check.buildMessage(event), true).queue();
                    throw new CommandCheckException();
                }
            }

            ArgumentParser argumentParser = new ArgumentParser(event, args);

            if (argumentParser.parseArguments()) {
                for (CommandFinalizer finalizer : CommandFinalizer.getFinalizers()) {
                    finalizer.finalize(event);
                }

                try {
                    command.run(event);
                } catch (Exception e) {
                    if (!(e instanceof CommandException)) e.printStackTrace();
                    event.performReply(
                        new PresetBuilder(PresetType.ERROR,
                            e instanceof CommandException ?
                                e.getMessage() :
                                "Unknown error, please report it to a developer."
                        )).queue();
                }
            }

        } catch (CommandCheckException ignored) {
        }
    }
}
