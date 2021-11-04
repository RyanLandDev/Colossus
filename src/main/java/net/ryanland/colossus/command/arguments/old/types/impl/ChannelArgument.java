package net.ryanland.colossus.command.arguments.old.types.impl;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.command.arguments.old.types.SingleArgument;
import net.ryanland.colossus.events.MessageCommandEvent;

public class ChannelArgument extends SingleArgument<MessageChannel> {

    @Override
    public OptionType getType() {
        return OptionType.CHANNEL;
    }

    @Override
    public MessageChannel parsed(OptionMapping argument, MessageCommandEvent event) throws ArgumentException {
        return argument.getAsMessageChannel();
    }
}
