package net.ryanland.colossus.command.arguments.old.types.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.old.types.SingleArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

public class MemberArgument extends SingleArgument<Member> {

    @Override
    public Member parsed(OptionMapping argument, MessageCommandEvent event) throws ArgumentException {
        return argument.getAsMember();
    }
}
