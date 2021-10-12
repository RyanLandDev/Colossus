package net.ryanland.colossus.bot.command.arguments.types.impl;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.bot.command.arguments.types.SingleArgument;
import net.ryanland.colossus.bot.events.CommandEvent;

public class StringArgument extends SingleArgument<String> {

    @Override
    public String parsed(OptionMapping argument, CommandEvent event) {
        return argument.getAsString();
    }
}
