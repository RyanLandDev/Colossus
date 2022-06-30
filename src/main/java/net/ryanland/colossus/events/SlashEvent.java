package net.ryanland.colossus.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.ryanland.colossus.Colossus;
import net.ryanland.colossus.command.Command;
import net.ryanland.colossus.command.SubCommandHolder;
import net.ryanland.colossus.command.arguments.ParsedArgumentMap;
import net.ryanland.colossus.sys.entities.ColossusGuild;
import net.ryanland.colossus.sys.entities.ColossusMember;
import net.ryanland.colossus.sys.entities.ColossusUser;
import net.ryanland.colossus.sys.interactions.button.ButtonRow;
import net.ryanland.colossus.sys.message.PresetBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SlashEvent extends CommandEvent {

    private Command command;
    private SubCommandHolder headSubCommandHolder;
    private SubCommandHolder nestedSubCommandHolder;
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
    public SubCommandHolder getHeadSubCommandHolder() {
        return headSubCommandHolder;
    }

    @Override
    public SubCommandHolder getNestedSubCommandHolder() {
        return nestedSubCommandHolder;
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

    public void reply(MessageEmbed embed, boolean ephemeral, List<ActionRow> actionRows) {
        if (actionRows == null) actionRows = new ArrayList<>();
        event.replyEmbeds(embed)
            .setEphemeral(ephemeral)
            .addActionRows(actionRows)
            .queue();
    }

    @Override
    public void reply(Message message, boolean ephemeral) {
        event.reply(message).setEphemeral(ephemeral).queue();
    }

    @Override
    public void reply(String message, boolean ephemeral) {
        event.reply(message).setEphemeral(ephemeral).queue();
    }

    @Override
    public void reply(MessageEmbed embed, boolean ephemeral) {
        event.replyEmbeds(embed).setEphemeral(ephemeral).queue();
    }

    @Override
    public void reply(PresetBuilder embed) {
        // send reply and set hook
        InteractionHook hook = event.replyEmbeds(embed.embed())
            .setEphemeral(embed.isEphemeral())
            .addActionRows(embed.getButtonRows().stream().map(ButtonRow::toActionRow).collect(Collectors.toList()))
            .setContent(embed.getContent())
            .complete();
        // add listener for the buttons
        if (!embed.getButtonRows().isEmpty()) {
            ClickButtonEvent.addListener(
                hook.retrieveOriginal().complete().getIdLong(), embed.getButtonRows(),
                () -> hook.editOriginalComponents(Collections.emptyList()).queue()
            );
        }
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(event.getUser());
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

    @Override
    public boolean isFromGuild() {
        return event.isFromGuild();
    }

    @Override
    public JDA getJDA() {
        return event.getJDA();
    }

    @Override
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
    public ColossusGuild getGuild() {
        return new ColossusGuild(event.getGuild());
    }

    public Interaction getInteraction() {
        return event.getInteraction();
    }

    @Override
    public ColossusMember getMember() {
        return new ColossusMember(event.getMember());
    }

    public OffsetDateTime getTimeCreated() {
        return event.getTimeCreated();
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

}
