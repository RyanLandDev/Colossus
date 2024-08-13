package dev.ryanland.colossus.command.arguments.parsing;

import dev.ryanland.colossus.Colossus;
import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.arguments.Argument;
import dev.ryanland.colossus.command.arguments.ArgumentSet;
import dev.ryanland.colossus.command.arguments.ParsedArgumentMap;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.regular.SubCommand;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.events.command.CommandEvent;
import dev.ryanland.colossus.events.command.SlashCommandEvent;
import dev.ryanland.colossus.sys.presetbuilder.PresetBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayDeque;
import java.util.Deque;

public non-sealed class SlashCommandArgumentParser extends ArgumentParser {

    public SlashCommandArgumentParser(CommandEvent event) {
        super(event);
    }

    private SlashCommandEvent getEvent() {
        return (SlashCommandEvent) event;
    }

    public Deque<OptionMapping> getArgumentQueue() {
        Deque<OptionMapping> queue = new ArrayDeque<>(getEvent().getOptions());
        // subcommands
        if (event.getCommand() instanceof SubCommand) {
            queue.remove();
            // (nested) subcommand group
            if (event.getCommand() instanceof SubCommandHolder)
                queue.remove();
        }
        return queue;
    }

    @Override
    public boolean parseArguments() {
        Deque<OptionMapping> queue = new ArrayDeque<>(getEvent().getOptions());
        ParsedArgumentMap parsedArgs = new ParsedArgumentMap();

        Command command = event.getCommand();
        PresetBuilder embed = new PresetBuilder(Colossus.getErrorPresetType());

        // failsafe for if getArguments returns null
        ArgumentSet arguments = command.getArguments();
        if (arguments == null) arguments = new ArgumentSet();

        for (Argument<?> arg : arguments.values()) {
            try {
                Object parsedArg;
                if (queue.peek() == null && arg.isOptional())
                    parsedArg = arg.getOptionalValue(event);
                else
                    parsedArg = arg.resolveSlashCommandArgument(getEvent(), queue);

                parsedArgs.put(arg.getName(), parsedArg);

            } catch (MalformedArgumentException e) {
                event.reply(embed
                        .setDescription(e.getMessage(event, arg))
                        .setTitle("Invalid Argument")
                        .setEphemeral(true)
                );
                return false;
            } catch (ArgumentException ignored) {
            }
        }

        event.setParsedArgs(parsedArgs);
        return true;
    }
}