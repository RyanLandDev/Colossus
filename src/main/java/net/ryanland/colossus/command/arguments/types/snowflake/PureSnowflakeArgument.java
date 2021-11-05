package net.ryanland.colossus.command.arguments.types.snowflake;

import net.dv8tion.jda.api.entities.ISnowflake;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.events.CommandEvent;

public abstract class PureSnowflakeArgument<T extends ISnowflake> extends ArgumentStringResolver<T> {

    @Override
    public T resolve(String arg, CommandEvent event) throws ArgumentException {
        try {
            return resolve(event, arg);
        } catch (NumberFormatException e) {
            throw new MalformedArgumentException("Invalid ID provided, or not found.");
        }
    }

    public abstract T resolve(CommandEvent event, String id) throws ArgumentException;
}
