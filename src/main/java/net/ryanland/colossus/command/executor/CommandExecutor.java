package net.ryanland.colossus.command.executor;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.RestAction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.command.finalizers.Finalizer;
import net.ryanland.colossus.command.inhibitors.Inhibitor;
import net.ryanland.colossus.command.inhibitors.InhibitorException;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        Class<? extends Command> cmdClass = command.getClass();

        String paramName = "";
        Method runMethod = null;
        for (Method method : cmdClass.getMethods()) {
            if (method.getName().equals("run")) {
                paramName = method.getParameterTypes()[0].getName();
                if (event instanceof SlashEvent && paramName.endsWith("SlashEvent")) runMethod = method;
                else if (event instanceof MessageCommandEvent && paramName.endsWith("MessageCommandEvent")) runMethod = method;
            }
        }

        MessageCommandEvent eventAsMessageCommand;
        SlashEvent eventAsSlashCommand;
        Method replyMethod = null;
        if (event instanceof SlashEvent) {
            eventAsSlashCommand = (SlashEvent) event;
            replyMethod = getReplyMethod(eventAsSlashCommand);
            
            if (eventAsSlashCommand.getSubCommandGroup() != null) {
                command = command.getInfo().getSubCommandGroupMap().get(eventAsSlashCommand.getSubCommandGroup()).getSubCommand(eventAsSlashCommand.getSubCommandName());
            } else if (eventAsSlashCommand.getSubCommandName() != null) {
                command = command.getInfo().getSubCommandMap().get(eventAsSlashCommand.getSubCommandName());
            }
        } else if (event instanceof MessageCommandEvent) {
            eventAsMessageCommand = (MessageCommandEvent) event;
            replyMethod = getReplyMethod(eventAsMessageCommand);
            
        }

        event.setCommand(command);

        try {
            for (Inhibitor inhibitor : Colossus.getInhibitors()) {
                if (inhibitor.check(event)) {
                    event.reply(inhibitor.buildMessage(event));
                    throw new InhibitorException();
                }
            }

            ArgumentParser argumentParser = new ArgumentParser(event, args);

            if (argumentParser.parseArguments()) {
                try {

                    if (runMethod != null) runMethod.invoke(event);

                } catch (InvocationTargetException e) {
                    PresetBuilder builder = new PresetBuilder(Colossus.getErrorPresetType(),
                                    e.getMessage()
                    );

                    
                    ((RestAction<?>) replyMethod.invoke(builder)).queue();
                    return;
                }

                for (Finalizer finalizer : Colossus.getFinalizers()) {
                    finalizer.finalize(event);
                }
            }

        } catch (InhibitorException | IllegalAccessException | InvocationTargetException ignored) {
        }
    }
    
    private static Method getReplyMethod(CommandEvent event) {
        Class<? extends CommandEvent> eventClass = event.getClass();
        for (Method m : eventClass.getMethods()) {
            if (m.getName().equals("performReply")) {
                if (m.getParameterCount() == 1 && m.getParameterTypes()[0] == PresetBuilder.class) return m;
            }
        }
    }
}
