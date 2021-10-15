package net.ryanland.colossus.bot.command.executor;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.command.arguments.parsing.ArgumentParser;
import net.ryanland.colossus.bot.command.finalizers.Finalizer;
import net.ryanland.colossus.bot.command.inhibitors.Inhibitor;
import net.ryanland.colossus.bot.command.inhibitors.InhibitorException;
import net.ryanland.colossus.bot.command.CommandException;
import net.ryanland.colossus.bot.command.Command;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;

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
            for (Inhibitor inhibitor : Colossus.getInhibitors()) {
                if (inhibitor.check(event)) {
                    event.performReply(inhibitor.buildMessage(event), true).queue();
                    throw new InhibitorException();
                }
            }

            ArgumentParser argumentParser = new ArgumentParser(event, args);

            if (argumentParser.parseArguments()) {
                try {
                    command.run(event);
                } catch (Exception e) {
                    if (!(e instanceof CommandException))
                        e.printStackTrace();
                    event.performReply(
                        new PresetBuilder(Colossus.getErrorPresetType(),
                            e instanceof CommandException ?
                                e.getMessage() :
                                "Unknown error, please report it to a developer."
                        )).queue();
                    return;
                }

                for (Finalizer finalizer : Colossus.getFinalizers()) {
                    finalizer.finalize(event);
                }
            }

        } catch (InhibitorException ignored) {
        }
    }
}
