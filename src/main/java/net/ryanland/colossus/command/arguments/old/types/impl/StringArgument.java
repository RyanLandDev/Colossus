package net.ryanland.colossus.command.arguments.old.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.old.types.SingleArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

public class StringArgument extends SingleArgument<String> {

    @Override
    public String parsed(OptionMapping argument, MessageCommandEvent event) {
        return argument.getAsString();
    }
}
