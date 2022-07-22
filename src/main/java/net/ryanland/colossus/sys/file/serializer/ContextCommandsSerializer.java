package net.ryanland.colossus.sys.file.serializer;

import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.command.context.ContextCommandType;
import net.ryanland.colossus.command.executor.CommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * db format: "TYPE_ID;CMD_NAME"
 */
public class ContextCommandsSerializer implements Serializer<List<String>, List<ContextCommand<?>>> {

    private static final ContextCommandsSerializer instance = new ContextCommandsSerializer();

    public static ContextCommandsSerializer getInstance() {
        return instance;
    }

    @Override
    public List<String> serialize(@NotNull List<ContextCommand<?>> toSerialize) {
        return toSerialize.stream()
            .map(contextCommand -> contextCommand.getType().getId() + ";" + contextCommand.getName())
            .collect(Collectors.toList());
    }

    @Override
    public List<ContextCommand<?>> deserialize(@NotNull List<String> toDeserialize) {
        return toDeserialize.stream()
            .map(s -> {
                String[] elements = s.split(";", 2);
                ContextCommandType type = ContextCommandType.of(Integer.parseInt(elements[0]));
                return CommandHandler.getContextCommand(type, elements[1]);
            })
            .collect(Collectors.toList());
    }
}
