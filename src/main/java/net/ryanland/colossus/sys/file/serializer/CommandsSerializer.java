package net.ryanland.colossus.sys.file.serializer;

import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.executor.CommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class CommandsSerializer implements Serializer<List<String>, List<Command>> {

    private static final CommandsSerializer instance = new CommandsSerializer();

    public static CommandsSerializer getInstance() {
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
