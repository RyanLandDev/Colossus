package net.ryanland.colossus.command.arguments.parsing;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.Argument;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.SlashEvent;
import net.ryanland.colossus.sys.message.DefaultPresetType;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

public class SlashCommandArgumentParser extends ArgumentParser {

    public SlashCommandArgumentParser(CommandEvent event) {
        super(event);
    }

    private SlashEvent getEvent() {
        return (SlashEvent) event;
    }

    @Override
    public boolean parseArguments() {
        Deque<OptionMapping> queue = new ArrayDeque<>(getEvent().getOptions());
        ParsedArgumentMap parsedArgs = new ParsedArgumentMap();

        Command command = event.getCommand();
        PresetBuilder embed = new PresetBuilder(DefaultPresetType.ERROR);

        for (Argument<?> arg : command.getArguments()) {
            try {
                Object parsedArg;
                if (queue.peek() == null && arg.isOptional())
                    parsedArg = arg.getOptionalFunction().apply(event);
                else
                    parsedArg = arg.resolveSlashCommandArgument(queue, getEvent());

                parsedArgs.put(arg.getId(), parsedArg);

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