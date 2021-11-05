package net.ryanland.colossus.command.executor;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
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
import java.lang.reflect.Method;

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

        String paramName;
        Method runMethod = null;
        for (Method method : cmdClass.getMethods()) { //Finding the correct run method
            if (method.getName().equals("run")) {
                paramName = method.getParameterTypes()[0].getName();

                //A new else if statement for all types of commands is necessary, no automatisation possible
                if (event instanceof SlashEvent && paramName.endsWith("SlashEvent")) runMethod = method;
                else if (event instanceof MessageCommandEvent && paramName.endsWith("MessageCommandEvent")) runMethod = method;
            }
        }

        //Getting the event to their original event for possible use later in exception handling
        MessageCommandEvent eventAsMessageCommand;
        SlashEvent eventAsSlashCommand;
        ArgumentParser argumentParser = null;
        if (event instanceof SlashEvent) {
            eventAsSlashCommand = (SlashEvent) event;
            argumentParser = new SlashCommandArgumentParser(event);

            //Applying different command if subcommand is used
            if (eventAsSlashCommand.getSubCommandGroup() != null) {
                command = command.getInfo().getSubCommandGroupMap()
                    .get(eventAsSlashCommand.getSubCommandGroup()).getSubCommand(eventAsSlashCommand.getSubCommandName());
            } else if (eventAsSlashCommand.getSubCommandName() != null) {
                command = command.getInfo().getSubCommandMap()
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
                    if (runMethod != null) runMethod.invoke(event);
                } catch (InvocationTargetException exception) {
                    Throwable e = exception.getTargetException();
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

                for (Finalizer finalizer : Colossus.getFinalizers()) {
                    finalizer.finalize(event);
                }
            }

        } catch (InhibitorException | IllegalAccessException ignored) {
        }
    }
}
