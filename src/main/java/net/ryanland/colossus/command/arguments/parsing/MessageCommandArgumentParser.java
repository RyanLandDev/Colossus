package net.ryanland.colossus.command.arguments.parsing;

import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ArgumentSet;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MissingArgumentException;
import net.ryanland.colossus.command.regular.SubCommand;
import net.ryanland.colossus.events.command.CommandEvent;
import net.ryanland.colossus.events.command.MessageCommandEvent;
import net.ryanland.colossus.sys.presetbuilder.PresetBuilder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public non-sealed class MessageCommandArgumentParser extends ArgumentParser {

    public MessageCommandArgumentParser(CommandEvent event) {
        super(event);
    }

    private MessageCommandEvent getEvent() {
        return (MessageCommandEvent) event;
    }

    /**
     * Create a queue of arguments, based on the message content split by spaces,
     * with the prefix and first word (command name) removed
     */
    public Deque<String> getRawArgumentQueue() {
        String content = getEvent().getMessage().getContentRaw();
        Deque<String> queue = new ArrayDeque<>(List.of(
            content.replaceFirst("(^<@(!|)" + Colossus.getSelfUser().getId() + ">\\s*)|(^" +
                    Pattern.quote(event.getUsedPrefix()) + ")", "")
                .split("\\s+")));
        queue.remove();
        return queue;
    }

    public Deque<String> getArgumentQueue() {
        Deque<String> queue = getRawArgumentQueue();
        // Remove extra words for subcommands
        if (event.getCommand() instanceof SubCommand) {
            queue.remove();
            // (nested) subcommand group
            if (event.getNestedSubCommandHolder() != null)
                queue.remove();
        }
        return queue;
    }

    @Override
    public boolean parseArguments() {
        Deque<String> queue = getArgumentQueue();
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
                    parsedArg = arg.getOptionalFunction().apply(event);
                else if (queue.peek() == null && !arg.ignoreMissingException())
                    throw new MissingArgumentException();
                else
                    parsedArg = arg.resolveMessageCommandArgument(getEvent(), queue);

                parsedArgs.put(arg.getName(), parsedArg);

            } catch (MissingArgumentException e) {
                event.reply(embed
                    .setDescription(e.getMessage(event, arg))
                    .setTitle("Missing Argument")
                );
                return false;
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
