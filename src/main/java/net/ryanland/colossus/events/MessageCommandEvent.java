package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.sys.interactions.button.ButtonRow;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Collections;
import java.util.stream.Collectors;

public class MessageCommandEvent extends CommandEvent {

    private Command command;
    private SubCommandHolder headSubCommandHolder;
    private SubCommandHolder nestedSubCommandHolder;
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
    public SubCommandHolder getHeadSubCommandHolder() {
        return headSubCommandHolder;
    }

    @Override
    public SubCommandHolder getNestedSubCommandHolder() {
        return nestedSubCommandHolder;
    }

    @Override
    public String getName() {
        if (event.getMessage().getContentRaw() == null)
            throw new IllegalStateException("You must have the Message Content intent enabled to use this feature");
        try {
            return event.getMessage().getContentRaw().split("\\s+")[0].substring(getGuildPrefix().length()).toLowerCase();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public void setHeadSubCommandHolder(SubCommandHolder subCommandHolder) {
        this.headSubCommandHolder = subCommandHolder;
    }

    @Override
    public void setNestedSubCommandHolder(SubCommandHolder subCommandHolder) {
        this.nestedSubCommandHolder = subCommandHolder;
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

    @Override
    public void reply(Message message, boolean ephemeral) {
        event.getMessage().reply(message).queue();
    }

    @Override
    public void reply(String message, boolean ephemeral) {
        event.getMessage().reply(message).queue();
    }

    @Override
    public void reply(MessageEmbed embed, boolean ephemeral) {
        event.getMessage().replyEmbeds(embed).queue();
    }

    @Override
    public void reply(PresetBuilder embed) {
        // send reply
        Message message = event.getMessage().replyEmbeds(embed.embed())
            .setActionRows(embed.getButtonRows().stream().map(ButtonRow::toActionRow).collect(Collectors.toList()))
            .content(embed.getContent())
            .complete();
        // add listener for the buttons
        if (!embed.getButtonRows().isEmpty()) {
            ClickButtonEvent.addListener(message.getIdLong(), embed.getButtonRows(),
                () -> message.editMessageComponents(Collections.emptyList()).queue()
            );
        }
    }

    @Override
    public User getUser() {
        return event.getAuthor();
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    @Override
    public JDA getJDA() {
        return event.getJDA();
    }

    @Override
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
