package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.command.executor.functional_interface.CommandConsumer;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.message.PresetBuilder;

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
    public DiscordLocale getUserLocale() {
        return Colossus.DEFAULT_LOCALE; // we cant get the user locale with message commands, so return the default
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
        event.getMessage().replyEmbeds(embed.embed())
            .setActionRows(embed.getActionRows())
            .content(embed.getContent())
            .queue(embed::addComponentRowListeners);
    }

    /**
     * Reply to this event with a {@link Modal}<br>
     * Note: When overriding this method, do not forget to add a modal submit listener!
     *
     * @param modal
     * @param action
     * @see ModalSubmitEvent
     * @see ModalSubmitEvent#addListener(Long, String, CommandConsumer)
     */
    @Override
    public void reply(Modal modal, CommandConsumer<ModalSubmitEvent> action) {
        throw new IllegalStateException("Message commands do not support replying with modals");
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(event.getAuthor());
    }

    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(event.getGuild());
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
    public ColossusMember getMember() {
        return new ColossusMember(event.getMember());
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
