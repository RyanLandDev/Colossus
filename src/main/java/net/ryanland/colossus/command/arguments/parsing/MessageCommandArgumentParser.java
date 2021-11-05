package net.ryanland.colossus.command.arguments.parsing;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class MessageCommandArgumentParser extends ArgumentParser {

    public MessageCommandArgumentParser(CommandEvent event) {
        super(event);
    }

    private MessageCommandEvent getEvent() {
        return (MessageCommandEvent) event;
    }

    @Override
    public boolean parseArguments() {
        // Create a queue of arguments, based on the message content split by spaces,
        // with the prefix and first word (command name) removed
        String content = getEvent().getMessage().getContentRaw();
        Deque<String> queue = new ArrayDeque<>(List.of(
            content.replaceFirst("(^<@(!|)" + Colossus.getSelfUser().getId() + ">\\s*)|(^" +
                Pattern.quote(event.getGuildPrefix()), "")
                .split("\\s+")));
        queue.remove();
        ParsedArgumentMap parsedArgs = new ParsedArgumentMap();

        Command command = event.getCommand();
        PresetBuilder embed = new PresetBuilder(DefaultPresetType.ERROR);

        for (Argument<?> arg : command.getArguments()) {
            try {
                Object parsedArg;
                if (queue.peek() == null && arg.isOptional())
                    parsedArg = arg.getOptionalFunction().apply(event);
                else
                    parsedArg = arg.resolveMessageCommandArgument(queue, getEvent());

                parsedArgs.put(arg.getId(), parsedArg);

            } catch (MalformedArgumentException e) {
                event.reply(embed
                    .setDescription(e.getMessage(event, arg))
                    .setTitle("Invalid Argument")
                );
                return false;
            } catch (ArgumentException ignored) {
            }
        }

        event.setParsedArgs(parsedArgs);
        return true;
    }
}
