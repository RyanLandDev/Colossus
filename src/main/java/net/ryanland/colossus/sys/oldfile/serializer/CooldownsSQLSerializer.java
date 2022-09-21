package net.ryanland.colossus.sys.oldfile.serializer;

import net.ryanland.colossus.command.CommandType;
import net.ryanland.colossus.command.cooldown.Cooldown;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * See {@link net.ryanland.colossus.sys.oldfile.database.SQLDatabaseDriver} for the data model
 */
public class CooldownsSQLSerializer implements Serializer<String, List<Cooldown>> {

    private static final CooldownsSQLSerializer INSTANCE = new CooldownsSQLSerializer();

    public static CooldownsSQLSerializer getInstance() {
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
