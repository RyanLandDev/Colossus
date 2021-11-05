package net.ryanland.colossus.command.arguments.types;

import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.primitive.ArgumentStringResolver;
import net.ryanland.colossus.events.CommandEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateArgument extends ArgumentStringResolver<Date> {

    public final SimpleDateFormat format;

    /**
     * Asks the user for a date in the {@link SimpleDateFormat} {@code dd/MM/yyyy}.
     * <br>For a custom format, use the {@link #DateArgument(String)} constructor instead.
     */
    public DateArgument() {
        this.format = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Asks the user for a date.
     * @param format The {@link SimpleDateFormat} the user should provide a date in
     */
    public DateArgument(String format) {
        this.format = new SimpleDateFormat(format);
    }

    @Override
    public Date resolve(String arg, CommandEvent event) throws ArgumentException {
        try {
            return format.parse(arg);
        } catch (ParseException e) {
            throw new MalformedArgumentException("Invalid date provided. Format: " + format.toPattern() + ".");
        }
    }
}
