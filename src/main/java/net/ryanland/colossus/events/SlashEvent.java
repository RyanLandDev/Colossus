package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenuBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public class SlashEvent extends CommandEvent {

    private Command command;
    private ParsedArgumentMap parsedArgs;
    private final SlashCommandEvent event;

    public SlashEvent(SlashCommandEvent event) {
        this.event = event;
    }

    @Override
    public Command getCommand() {
        return command;
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

    private ReplyAction sendReply(Message message, boolean ephemeral) {
        return event.reply(message).setEphemeral(ephemeral);
    }

    private ReplyAction sendReply(Message message) {
        return sendReply(message, false);
    }

    private ReplyAction sendReply(MessageEmbed embed, boolean ephemeral) {
        return event.replyEmbeds(embed)
            .setEphemeral(ephemeral);
    }

    private ReplyAction sendReply(MessageEmbed embed) {
        return sendReply(embed, false);
    }

    private ReplyAction sendReply(String message, boolean ephemeral) {
        return event.reply(message).setEphemeral(ephemeral);
    }

    private ReplyAction sendReply(String message) {
        return sendReply(message, false);
    }

    private ReplyAction sendReply(PresetBuilder embed, boolean ephemeral) {
        return sendReply(embed.build(), ephemeral);
    }

    private ReplyAction sendReply(PresetBuilder embed) {
        return sendReply(embed, false);
    }

    public ReplyAction performReply(Message message) {
        return sendReply(message);
    }

    public ReplyAction performReply(Message message, boolean ephemeral) {
        return sendReply(message, ephemeral);
    }

    public ReplyAction performReply(String message) {
        return sendReply(message);
    }

    public ReplyAction performReply(String message, boolean ephemeral) {
        return sendReply(message, ephemeral);
    }

    public ReplyAction performReply(PresetBuilder embed) {
        return sendReply(embed, embed.isEphemeral());
    }

    public ReplyAction performReply(PresetBuilder embed, boolean ephemeral) {
        return sendReply(embed, ephemeral);
    }

    public ReplyAction performReply(MessageEmbed embed) {
        return sendReply(embed);
    }

    public ReplyAction performReply(MessageEmbed embed, boolean ephemeral) {
        return sendReply(embed, ephemeral);
    }

    @Override
    public void reply(Message message) {
        sendReply(message).queue();
    }

    public void reply(Message message, boolean ephemeral) {
        sendReply(message, ephemeral).queue();
    }

    @Override
    public void reply(String message) {
        sendReply(message).queue();
    }

    public void reply(String message, boolean ephemeral) {
        sendReply(message, ephemeral).queue();
    }

    @Override
    public void reply(MessageEmbed embed) {
        sendReply(embed).queue();
    }

    public void reply(MessageEmbed embed, boolean ephemeral) {
        sendReply(embed, ephemeral).queue();
    }

    @Override
    public void reply(PresetBuilder embed) {
        sendReply(embed, embed.isEphemeral()).queue();
    }

    public void reply(PresetBuilder embed, boolean ephemeral) {
        sendReply(embed, ephemeral).queue();
    }

    public void reply(InteractionMenu menu) throws CommandException {
        menu.send(getInteraction());
    }

    public void reply(InteractionMenuBuilder<?> menuBuilder) throws CommandException {
        reply(menuBuilder.build());
    }

    @Override
    public User getUser() {
        return event.getUser();
    }

    public List<OptionMapping> getOptions() {
        return event.getOptions();
    }

    public OptionMapping getOption(@NotNull String name) {
        return event.getOption(name);
    }

    public boolean isAcknowledged() {
        return event.isAcknowledged();
    }

    public InteractionHook getHook() {
        return event.getHook();
    }

    @Override
    public String getName() {
        return event.getName();
    }

    public String getSubCommandName() {
        return event.getSubcommandName();
    }

    public String getSubCommandGroup() {
        return event.getSubcommandGroup();
    }

    public ReplyAction deferReply() {
        return event.deferReply();
    }

    public ReplyAction deferReply(boolean ephemeral) {
        return event.deferReply(ephemeral);
    }

    public boolean isFromGuild() {
        return event.isFromGuild();
    }

    public JDA getJDA() {
        return event.getJDA();
    }

    public MessageChannel getChannel() {
        return event.getChannel();
    }

    public String getCommandId() {
        return event.getCommandId();
    }

    public long getCommandIdLong() {
        return event.getCommandIdLong();
    }

    public ChannelType getChannelType() {
        return event.getChannelType();
    }

    public GuildChannel getGuildChannel() {
        return event.getGuildChannel();
    }

    public String getCommandPath() {
        return event.getCommandPath();
    }

    public String getId() {
        return event.getId();
    }

    public long getIdLong() {
        return event.getIdLong();
    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    public Interaction getInteraction() {
        return event.getInteraction();
    }

    @Override
    public Member getMember() {
        return event.getMember();
    }

    public OffsetDateTime getTimeCreated() {
        return event.getTimeCreated();
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

}
