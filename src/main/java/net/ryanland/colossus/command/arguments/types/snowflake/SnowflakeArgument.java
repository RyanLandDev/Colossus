package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.command.MessageCommandEvent;

public abstract class SnowflakeArgument<T extends ISnowflake> extends SingleArgument<T> {

    @Override
    public final T resolveMessageCommandArgument(String arg, MessageCommandEvent event) throws ArgumentException {
        try {
            return resolveMessageCommandArgument(event, arg.replaceAll("\\D", ""));
        } catch (IllegalArgumentException e) {
            throw new MalformedArgumentException("Invalid ID/mention provided, or not found.");
        }
    }

    public abstract T resolveMessageCommandArgument(MessageCommandEvent event, String id) throws ArgumentException;
}
