package net.ryanland.colossus.bot.command.arguments.parsing;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.Argument;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.bot.command.impl.Command;
import net.ryanland.colossus.bot.events.CommandEvent;
import net.ryanland.colossus.sys.message.builders.PresetBuilder;
import net.ryanland.colossus.sys.message.builders.PresetType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ArgumentParser {

    private final CommandEvent event;
    private final List<OptionMapping> args;

    public ArgumentParser(CommandEvent event) {
        this.event = event;
        this.args = event.getOptions();
    }

    public ArgumentParser(CommandEvent event, List<OptionMapping> args) {
        this.event = event;
        this.args = args;
    }

    public boolean parseArguments() {
        Deque<OptionMapping> queue = new ArrayDeque<>(args);
        ParsedArgumentMap parsedArgs = new ParsedArgumentMap();

        Command command = event.getCommand();
        PresetBuilder embed = new PresetBuilder(PresetType.ERROR);

        for (Argument<?> arg : command.getArguments()) {

            try {
                Object parsedArg;
                if (queue.peek() == null && arg.isOptional()) {
                    parsedArg = arg.getOptionalFunction().apply(event);
                } else {
                    parsedArg = arg.parse(queue, event);
                }

                parsedArgs.put(arg.getId(), parsedArg);

            } catch (MalformedArgumentException e) {
                event.performReply(embed
                        .setDescription(e.getMessage(event, arg))
                        .setTitle("Invalid Argument")
                    , true).queue();
                return false;

            } catch (ArgumentException ignored) {
            }
        }

        event.setParsedArgs(parsedArgs);
        return true;
    }
}