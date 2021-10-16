package net.ryanland.colossus.command.arguments.parsing;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.events.ContentCommandEvent;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.message.DefaultPresetType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ArgumentParser {

    private final ContentCommandEvent event;
    private final List<OptionMapping> args;

    public ArgumentParser(ContentCommandEvent event) {
        this.event = event;
        this.args = event.getOptions();
    }

    public ArgumentParser(ContentCommandEvent event, List<OptionMapping> args) {
        this.event = event;
        this.args = args;
    }

    public boolean parseArguments() {
        Deque<OptionMapping> queue = new ArrayDeque<>(args);
        ParsedArgumentMap parsedArgs = new ParsedArgumentMap();

        Command command = event.getCommand();
        PresetBuilder embed = new PresetBuilder(DefaultPresetType.ERROR);

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