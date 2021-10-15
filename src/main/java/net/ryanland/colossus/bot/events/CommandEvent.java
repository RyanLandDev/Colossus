package net.ryanland.colossus.bot.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.bot.command.arguments.parsing.ParsedArgumentMap;
import net.ryanland.colossus.bot.command.CommandException;
import net.ryanland.colossus.bot.command.Command;
import net.ryanland.colossus.sys.file.DatabaseDriver;
import net.ryanland.colossus.sys.file.Table;
import net.ryanland.colossus.sys.message.PresetBuilder;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenu;
import net.ryanland.colossus.sys.interactions.menu.InteractionMenuBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

public class CommandEvent {

    private Command command;
    private ParsedArgumentMap parsedArgs;
    private final SlashCommandEvent event;

    public CommandEvent(SlashCommandEvent event) {
        this.event = event;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public ParsedArgumentMap getParsedArgs() {
        return parsedArgs;
    }

    public void setParsedArgs(ParsedArgumentMap parsedArgs) {
        this.parsedArgs = parsedArgs;
    }

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

    public void reply(Message message) {
        sendReply(message).queue();
    }

    public void reply(Message message, boolean ephemeral) {
        sendReply(message, ephemeral).queue();
    }

    public void reply(String message) {
        sendReply(message).queue();
    }

    public void reply(String message, boolean ephemeral) {
        sendReply(message, ephemeral).queue();
    }

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

    public User getUser() {
        return event.getUser();
    }

    /**
     * Get the {@link Table} of the user who executed this command.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Table
     * @see DatabaseDriver
     */
    public Table<User> getUserTable() {
        return Colossus.getDatabaseDriver().get(getUser());
    }

    /**
     * Get the {@link Table} of the guild this command was executed in.
     * This will be {@code null} if the command was executed in DMs.
     * <br>Note: This method may produce an error if the {@link DatabaseDriver} is not properly configured.
     * @see Table
     * @see DatabaseDriver
     */
    public Table<Guild> getGuildTable() {
        if (getGuild() == null)
            return null;
        return Colossus.getDatabaseDriver().get(getGuild());
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

    public Guild getGuild() {
        return event.getGuild();
    }

    public Interaction getInteraction() {
        return event.getInteraction();
    }

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
