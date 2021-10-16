package net.ryanland.colossus.command.arguments.types.impl;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.types.SingleArgument;
import net.ryanland.colossus.events.ContentCommandEvent;

public class UserArgument extends SingleArgument<User> {

    @Override
    public OptionType getType() {
        return OptionType.USER;
    }

    @Override
    public User parsed(OptionMapping argument, ContentCommandEvent event) throws ArgumentException {
        return argument.getAsUser();
    }
}
