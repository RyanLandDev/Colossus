package net.ryanland.colossus.bot.command.arguments.types.impl;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.ryanland.colossus.bot.command.arguments.parsing.exceptions.ArgumentException;
import net.ryanland.colossus.bot.command.arguments.types.SingleArgument;
import net.ryanland.colossus.bot.events.CommandEvent;

public class ChannelArgument extends SingleArgument<MessageChannel> {

    @Override
    public OptionType getType() {
        return OptionType.CHANNEL;
    }

    @Override
    public MessageChannel parsed(OptionMapping argument, CommandEvent event) throws ArgumentException {
        return argument.getAsMessageChannel();
    }
}
