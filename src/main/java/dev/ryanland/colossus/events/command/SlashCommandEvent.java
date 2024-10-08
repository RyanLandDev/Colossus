package dev.ryanland.colossus.events.command;

import dev.ryanland.colossus.command.Command;
import dev.ryanland.colossus.command.arguments.ParsedArgumentMap;
import dev.ryanland.colossus.command.regular.SubCommandHolder;
import dev.ryanland.colossus.events.repliable.InteractionRepliableEvent;
import dev.ryanland.colossus.sys.snowflake.ColossusGuild;
import dev.ryanland.colossus.sys.snowflake.ColossusMember;
import dev.ryanland.colossus.sys.snowflake.ColossusUser;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class SlashCommandEvent extends CommandEvent {

    private Command command;
    private SubCommandHolder headSubCommandHolder;
    private SubCommandHolder nestedSubCommandHolder;
    private ParsedArgumentMap parsedArgs;
    private final SlashCommandInteractionEvent event;

    public SlashCommandEvent(SlashCommandInteractionEvent event) {
        this.event = event;
        repliableEvent = (InteractionRepliableEvent) () -> event;
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
            .setComponents(actionRows)
            .queue();
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

    public ReplyCallbackAction deferReply() {
        return event.deferReply();
    }

    public ReplyCallbackAction deferReply(boolean ephemeral) {
        return event.deferReply(ephemeral);
    }

    @Override
    public boolean isFromGuild() {
        return event.isFromGuild();
    }

    @Override
    public DiscordLocale getUserLocale() {
        return getEvent().getUserLocale();
    }

    @Override
    public JDA getJDA() {
        return event.getJDA();
    }

    @Override
    public MessageChannelUnion getChannel() {
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

    public String getFullCommandName() {
        return event.getFullCommandName();
    }

    public String getId() {
        return event.getId();
    }

    public long getIdLong() {
        return event.getIdLong();
    }

    public Interaction getInteraction() {
        return event.getInteraction();
    }

    public OffsetDateTime getTimeCreated() {
        return event.getTimeCreated();
    }

    @Override
    public SlashCommandInteractionEvent getEvent() {
        return event;
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(getEvent().getUser());
    }

    @Override
    public ColossusMember getMember() {
        return new ColossusMember(getEvent().getMember());
    }

    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(getEvent().getGuild());
    }
}
