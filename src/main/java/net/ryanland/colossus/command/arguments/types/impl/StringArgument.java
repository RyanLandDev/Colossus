package net.ryanland.colossus.command.arguments.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.ContentCommandEvent;

public class StringArgument extends SingleArgument<String> {

    @Override
    public String parsed(OptionMapping argument, ContentCommandEvent event) {
        return argument.getAsString();
    }
}
