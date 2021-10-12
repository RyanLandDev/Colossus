package net.ryanland.colossus.util.file.serializer;

import net.ryanland.colossus.bot.command.executor.cooldown.Cooldown;
import net.ryanland.colossus.bot.command.impl.Command;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
                    COOLDOWN DATABASE STRUCTURE
                        0 - command name (String)
                        1 - cooldown expire (Date)
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
            .map(cooldown -> Arrays.asList(cooldown.command().getName(), cooldown.expires()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Cooldown> deserialize(@NotNull List<List> toDeserialize) {
        return toDeserialize.stream()
            .map(list -> new Cooldown(Command.of((String) list.get(0)), (Date) list.get(1)))
            .collect(Collectors.toList());
    }
}
