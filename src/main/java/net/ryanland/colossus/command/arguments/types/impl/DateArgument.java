package net.ryanland.colossus.command.arguments.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.parsing.exceptions.MalformedArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.ContentCommandEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateArgument extends SingleArgument<Date> {

    public final SimpleDateFormat format;

    public DateArgument() {
        this.format = new SimpleDateFormat("dd/MM/yyyy");
    }

    public DateArgument(String format) {
        this.format = new SimpleDateFormat(format);
    }

    @Override
    public Date parsed(OptionMapping argument, ContentCommandEvent event) throws ArgumentException {
        try {
            return format.parse(argument.getAsString());
        } catch (ParseException e) {
            throw new MalformedArgumentException("Invalid date provided. Format: " + format.toPattern() + ".");
        }
    }
}
