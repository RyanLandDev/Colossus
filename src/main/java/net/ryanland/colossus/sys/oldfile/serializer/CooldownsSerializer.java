package net.ryanland.colossus.sys.oldfile.serializer;

import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.command.cooldown.Cooldown;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
                    COOLDOWN DATABASE STRUCTURE
                      List with sublists, containing:
                        0 - command name (String)
                        1 - cooldown expire (Date)
                        2 - command type (int)
 */
@SuppressWarnings("all")
public class CooldownsSerializer implements Serializer<List<List>, List<Cooldown>> {

    private static final CooldownsSerializer INSTANCE = new CooldownsSerializer();

    public static CooldownsSerializer getInstance() {
        return INSTANCE;
    }

    @Override
    public List<List> serialize(@NotNull List<Cooldown> toSerialize) {
        return toSerialize.stream()
            .map(cooldown -> Arrays.asList(cooldown.command().getName(), cooldown.expires(), cooldown.command().getCommandType().getId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Cooldown> deserialize(@NotNull List<List> toDeserialize) {
        return toDeserialize.stream()
            .map(list -> new Cooldown(CommandType.of((int) list.get(2)).getCommand((String) list.get(0)), (Date) list.get(1)))
            .toList();
    }
}
