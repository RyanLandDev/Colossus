package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Arrays;

public class MessageCommandEvent extends CommandEvent {

    private Command command;
    private ParsedArgumentMap parsedArgs;
    private final MessageReceivedEvent event;

    public MessageCommandEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public String getName() {
        if (event.getMessage().getContentRaw() == null)
            throw new IllegalStateException("You must have the Message Content intent enabled to use this feature");
        try {
            return event.getMessage().getContentRaw().split("\\s+")[0].substring(getGuildPrefix().length());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public ParsedArgumentMap getParsedArgs() {
        return parsedArgs;
    }

    @Override
    public void setParsedArgs(ParsedArgumentMap parsedArgs) {
        this.parsedArgs = parsedArgs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getArgument(String id) {
        return (T) parsedArgs.get(id);
    }

    @Override
    public boolean isFromGuild() {
        return event.isFromGuild();
    }

    public MessageAction performReply(Message message) {
        return event.getMessage().reply(message);
    }

    public MessageAction performReply(String message) {
        return event.getMessage().reply(message);
    }

    public MessageAction performReply(MessageEmbed embed) {
        return event.getMessage().replyEmbeds(embed);
    }

    public MessageAction performReply(PresetBuilder embed) {
        return performReply(embed.build());
    }

    @Override
    public void reply(Message message) {
        performReply(message).queue();
    }

    @Override
    public void reply(String message) {
        performReply(message).queue();
    }

    @Override
    public void reply(MessageEmbed embed) {
        performReply(embed).queue();
    }

    @Override
    public void reply(PresetBuilder embed) {
        performReply(embed).queue();
    }

    @Override
    public User getUser() {
        return event.getAuthor();
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    public JDA getJDA() {
        return event.getJDA();
    }

    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public ChannelType getChannelType() {
        return event.getChannelType();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public Member getMember() {
        return event.getMember();
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
