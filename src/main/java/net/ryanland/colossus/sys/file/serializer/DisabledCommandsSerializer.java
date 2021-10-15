package net.ryanland.colossus.sys.file.serializer;

import net.ryanland.colossus.bot.command.Command;
import net.ryanland.colossus.bot.command.executor.CommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class DisabledCommandsSerializer implements Serializer<List<String>, List<Command>> {

    private static final DisabledCommandsSerializer instance = new DisabledCommandsSerializer();

    public static DisabledCommandsSerializer getInstance() {
        return instance;
    }

    @Override
    public List<String> serialize(@NotNull List<Command> toSerialize) {
        return toSerialize.stream()
            .map(Command::getName)
            .collect(Collectors.toList());
    }

    @Override
    public List<Command> deserialize(@NotNull List<String> toDeserialize) {
        return toDeserialize.stream()
            .map(CommandHandler::getCommand)
            .collect(Collectors.toList());
    }
}
