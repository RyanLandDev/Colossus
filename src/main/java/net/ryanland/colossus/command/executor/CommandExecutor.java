package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.MessageCommandArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.SlashCommandArgumentParser;
import net.ryanland.colossus.command.context.ContextCommandType;
import net.ryanland.colossus.command.finalizers.CommandFinalizer;
import net.ryanland.colossus.command.finalizers.ContextFinalizer;
import net.ryanland.colossus.command.inhibitors.CommandInhibitor;
import net.ryanland.colossus.command.inhibitors.ContextInhibitor;
import net.ryanland.colossus.command.inhibitors.InhibitorException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.ContextCommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

public class CommandExecutor {

    public void run(CommandEvent event) {
        Command command = CommandHandler.getCommand(event.getName());
        if (command == null) return;
        event.setCommand(command);

        execute(event);
    }

    @SuppressWarnings("all")
    public <T extends ISnowflake> void run(ContextCommandEvent<T> event) {
        ContextCommandType type = ContextCommandType.of(event.getTargetType());
        ContextCommand<T> contextCommand = (ContextCommand<T>) CommandHandler.getContextCommand(type, event.getName());
        if (contextCommand == null) return;
        event.setCommand(contextCommand);

        execute(event);
    }

    @SuppressWarnings("all")
    public void execute(CommandEvent event) {
        Command originalCommand = event.getCommand();
        Command command = originalCommand;
        Class<? extends Command> cmdClass = command.getClass();

        // Getting the event to their original event for possible use later in exception handling
        MessageCommandEvent eventAsMessageCommand = null;
        SlashCommandEvent eventAsSlashCommand = null;
        ArgumentParser argumentParser = null;
        if (event instanceof SlashCommandEvent) {
            eventAsSlashCommand = (SlashCommandEvent) event;
            argumentParser = new SlashCommandArgumentParser(event);

            // Applying a different command if a subcommand is used
            if (eventAsSlashCommand.getSubCommandName() != null) {
                SlashCommandEvent finalEventAsSlashCommand = eventAsSlashCommand;
                // if a subcommand group is used, define this first before finding the actual subcommand,
                // to prevent subcommands with duplicate names
                if (eventAsSlashCommand.getSubCommandGroup() != null) {
                    event.setHeadSubCommandHolder(((SubCommandHolder) command));
                    command = (Command) command.getSubCommands().stream()
                        .filter(subcommandGroup -> ((Command) subcommandGroup).getName()
                            .equals(finalEventAsSlashCommand.getSubCommandGroup()))
                        .findFirst().get();
                    event.setNestedSubCommandHolder((SubCommandHolder) command);
                }
                // finding the subcommand
                command = (Command) command.getSubCommands().stream()
                    .filter(subCommand -> ((Command) subCommand).getName().equals(finalEventAsSlashCommand.getSubCommandName()))
                    .findFirst().get();

                if (eventAsSlashCommand.getSubCommandGroup() == null)
                    event.setNestedSubCommandHolder((SubCommandHolder) originalCommand);
            }
        } else if (event instanceof MessageCommandEvent) {
            eventAsMessageCommand = (MessageCommandEvent) event;
            argumentParser = new MessageCommandArgumentParser(event);

            // Find the subcommand if one is used
            if (command instanceof SubCommandHolder) {
                Deque<String> queue = ((MessageCommandArgumentParser) argumentParser).getRawArgumentQueue();
                try {
                    event.setHeadSubCommandHolder((SubCommandHolder) command);
                    command = (Command) findSubcommand(event, command, queue);
                    if (command instanceof SubCommandHolder) {
                        event.setNestedSubCommandHolder((SubCommandHolder) command);
                        command = (Command) findSubcommand(event, command, queue);
                    }
                } catch (IllegalArgumentException e) {
                    return;
                }
            }
        }

        event.setCommand(command);
        cmdClass = command.getClass();

        try {
            // run inhibitors
            for (CommandInhibitor inhibitor : Colossus.getCommandInhibitors()) {
                if (inhibitor.check(event)) {
                    event.reply(inhibitor.buildMessage(event));
                    throw new InhibitorException();
                }
            }

            // parse args
            if (argumentParser.parseArguments()) {
                try {
                    //Invoking the run method, only the InvocationTargetException can be thrown through the method,
                    // if any exception is thrown while executing the code
                    //A new else if statement for all types of commands is necessary, no automatisation possible
                    if (event instanceof SlashCommandEvent)
                        ((SlashCommand) cmdClass.getDeclaredConstructor().newInstance()).run(eventAsSlashCommand);
                    else if (event instanceof MessageCommandEvent)
                        ((MessageCommand) cmdClass.getDeclaredConstructor().newInstance()).run(eventAsMessageCommand);
                } catch (NoSuchMethodException | CommandException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    if (!(e instanceof CommandException))
                        e.printStackTrace();

                    event.reply(
                        new PresetBuilder(Colossus.getErrorPresetType(),
                            e instanceof CommandException ?
                                e.getMessage() :
                                "Unknown error, please report it to a developer."
                        ));
                    return;
                }

                // run finalizers
                for (CommandFinalizer finalizer : Colossus.getCommandFinalizers()) {
                    finalizer.finalize(event);
                }
            }

        } catch (InhibitorException ignored) {
        }
    }

    private SubCommand findSubcommand(CommandEvent event, Command command, Deque<String> parameterQueue) {
        String parameter;
        try {
            parameter = parameterQueue.remove();
        } catch (NoSuchElementException e) {
            parameter = "";
        }
        String finalParameter = parameter;

        if (finalParameter.isEmpty()) {
            event.reply(new PresetBuilder(Colossus.getErrorPresetType(), "Invalid Subcommand",
                "This command requires a subcommand.\n\nPossible subcommands are: " +
                    ((SubCommandHolder) command).getFormattedSubCommands()));
            throw new IllegalArgumentException();
        }

        List<SubCommand> matches = command.getSubCommands().stream()
            .filter(subcommand -> ((Command) subcommand).getName().equalsIgnoreCase(finalParameter)).toList();

        if (matches.isEmpty()) {
            event.reply(new PresetBuilder(Colossus.getErrorPresetType(), "Invalid Subcommand",
                "`" + parameter + "` is not a valid subcommand.\n\nPossible subcommands are: " +
                    ((SubCommandHolder) command).getFormattedSubCommands()));
            throw new IllegalArgumentException();
        }
        return matches.get(0);
    }

    @SuppressWarnings("all")
    public <T extends ISnowflake> void execute(ContextCommandEvent<T> event) {
        ContextCommand<T> command = event.getCommand();

        try {
            // run inhibitors
            for (ContextInhibitor inhibitor : Colossus.getContextInhibitors()) {
                if (inhibitor.check(event)) {
                    event.reply(inhibitor.buildMessage(event));
                    throw new InhibitorException();
                }
            }

            // run command
            try {
                command.run(event);
            } catch (Exception e) {
                if (!(e instanceof CommandException)) {
                    e.printStackTrace();
                }

                event.reply(new PresetBuilder(Colossus.getErrorPresetType(),
                    e instanceof CommandException ? e.getMessage() : "Unknown error, please report it to a developer."));
                return;
            }

            // run finalizers
            for (ContextFinalizer finalizer : Colossus.getContextFinalizers()) {
                finalizer.finalize(event);
            }
        } catch (InhibitorException ignored) {}
    }

}
