package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.*;
import net.ryanland.colossus.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.MessageCommandArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.SlashCommandArgumentParser;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.impl.TestSubCommand;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.command.inhibitors.InhibitorException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class CommandExecutor {

    public void run(CommandEvent event) {
        Command command = CommandHandler.getCommand(event.getName());
        if (command == null) return;
        event.setCommand(command);

        execute(event);
    }

    @SuppressWarnings("all")
    public void execute(CommandEvent event) {
        Command originalCommand = event.getCommand();
        Command command = originalCommand;
        Class<? extends Command> cmdClass = command.getClass();

        // Getting the event to their original event for possible use later in exception handling
        MessageCommandEvent eventAsMessageCommand = null;
        SlashEvent eventAsSlashCommand = null;
        ArgumentParser argumentParser = null;
        if (event instanceof SlashEvent) {
            eventAsSlashCommand = (SlashEvent) event;
            argumentParser = new SlashCommandArgumentParser(event);

            // Applying a different command if a subcommand is used
            if (eventAsSlashCommand.getSubCommandGroup() != null || eventAsSlashCommand.getSubCommandName() != null) {
                SlashEvent finalEventAsSlashCommand = eventAsSlashCommand;
                command = (Command) ((SubCommandHolder) command).getRealSubCommands().stream()
                    .filter(subCommand -> ((Command) subCommand).getName().equals(finalEventAsSlashCommand.getSubCommandName()))
                    .findFirst().get();
            }
        } else if (event instanceof MessageCommandEvent) {
            eventAsMessageCommand = (MessageCommandEvent) event;
            argumentParser = new MessageCommandArgumentParser(event);

            // Find the subcommand if one is used
            if (command instanceof SubCommandHolder) {
                Deque<String> queue = ((MessageCommandArgumentParser) argumentParser).getRawArgumentQueue();
                command = (Command) findSubcommand(event, command, queue.remove());
                if (command instanceof SubCommandHolder)
                    command = (Command) findSubcommand(event, command, queue.remove());
            }
        }

        event.setCommand(command);

        try {
            for (Inhibitor inhibitor : Colossus.getInhibitors()) {
                if (inhibitor.check(event)) {
                    event.reply(inhibitor.buildMessage(event));
                    throw new InhibitorException();
                }
            }

            if (argumentParser.parseArguments()) {
                try {
                    //Invoking the run method, only the InvocationTargetException can be thrown through the method,
                    // if any exception is thrown while executing the code
                    //A new else if statement for all types of commands is necessary, no automatisation possible
                    if (event instanceof SlashEvent)
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

                for (Finalizer finalizer : Colossus.getFinalizers())
                    finalizer.finalize(event);
            }

        } catch (InhibitorException ignored) {
        }
    }

    private SubCommand findSubcommand(CommandEvent event, Command command, String parameter) {
        List<SubCommand> matches = ((SubCommandHolder) command).getSubCommands().stream()
            .filter(subcommand -> ((Command) subcommand).getName().equalsIgnoreCase(parameter)).collect(Collectors.toList());
        if (matches.isEmpty()) {
            event.reply(new PresetBuilder(Colossus.getErrorPresetType(), "Invalid Subcommand",
                "`" + parameter + "` is not a valid subcommand. Possible subcommands are: " +
                    ((SubCommandHolder) command).getFormattedSubCommands()));
            throw new IllegalArgumentException();
        }
        return matches.get(0);
    }
}
