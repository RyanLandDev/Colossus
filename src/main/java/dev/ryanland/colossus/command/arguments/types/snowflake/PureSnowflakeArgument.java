package dev.ryanland.colossus.command.arguments.types.snowflake;

import dev.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import dev.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import dev.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import dev.ryanland.colossus.events.command.CommandEvent;
import net.dv8tion.jda.api.entities.ISnowflake;

public abstract class PureSnowflakeArgument<T extends ISnowflake> extends ArgumentStringResolver<T> {

    @Override
    public final T resolve(String arg, CommandEvent event) throws ArgumentException {
        try {
            return resolve(event, arg);
        } catch (NumberFormatException e) {
            throw new MalformedArgumentException("Invalid ID provided, or not found.");
        }
    }

    public abstract T resolve(CommandEvent event, String id) throws ArgumentException;
}
