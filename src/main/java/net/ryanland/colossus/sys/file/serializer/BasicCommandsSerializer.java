package net.ryanland.colossus.sys.file.serializer;

import net.ryanland.colossus.command.BasicCommand;
import net.ryanland.colossus.command.CommandType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * db format: "TYPE_ID;CMD_NAME"
 */
public class BasicCommandsSerializer implements Serializer<List<String>, List<BasicCommand>> {

    private static final BasicCommandsSerializer instance = new BasicCommandsSerializer();

    public static BasicCommandsSerializer getInstance() {
        return instance;
    }

    @Override
    public List<String> serialize(@NotNull List<BasicCommand> toSerialize) {
        return toSerialize.stream()
            .map(command -> command.getCommandType().getId() + ";" + command.getName())
            .toList();
    }

    @Override
    public List<BasicCommand> deserialize(@NotNull List<String> toDeserialize) {
        return toDeserialize.stream()
            .map(s -> {
                String[] elements = s.split(";", 2);
                CommandType type = CommandType.of(Integer.parseInt(elements[0]));
                return type.getCommand(elements[1]);
            })
            .collect(Collectors.toList());
    }
}
