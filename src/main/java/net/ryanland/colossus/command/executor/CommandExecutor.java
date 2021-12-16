package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.MessageCommand;
import net.ryanland.colossus.command.SlashCommand;
import net.ryanland.colossus.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.MessageCommandArgumentParser;
import net.ryanland.colossus.command.arguments.parsing.SlashCommandArgumentParser;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.command.inhibitors.InhibitorException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.lang.reflect.InvocationTargetException;

public class CommandExecutor {

    public void run(CommandEvent event) {
        Command command = CommandHandler.getCommand(event.getName());
        if (command == null) return;
        event.setCommand(command);

        execute(event);
    }

    public void execute(CommandEvent event) {
        Command command = event.getCommand();
        Class<? extends Command> cmdClass = command.getClass(); //Getting the command class

        //Getting the event to their original event for possible use later in exception handling
        MessageCommandEvent eventAsMessageCommand = null;
        SlashEvent eventAsSlashCommand = null;
        ArgumentParser argumentParser = null;
        if (event instanceof SlashEvent) {
            eventAsSlashCommand = (SlashEvent) event;
            argumentParser = new SlashCommandArgumentParser(event);

            //Applying different command if subcommand is used
            if (eventAsSlashCommand.getSubCommandGroup() != null) {
                command = command.getSubCommandGroupMap()
                    .get(eventAsSlashCommand.getSubCommandGroup()).getSubCommand(eventAsSlashCommand.getSubCommandName());
            } else if (eventAsSlashCommand.getSubCommandName() != null) {
                command = command.getSubCommandMap()
                    .get(eventAsSlashCommand.getSubCommandName());
            }
        } else if (event instanceof MessageCommandEvent) {
            eventAsMessageCommand = (MessageCommandEvent) event;
            argumentParser = new MessageCommandArgumentParser(event);
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
}
