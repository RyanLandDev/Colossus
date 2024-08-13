package net.ryanland.colossus.events.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction;
import net.ryanland.colossus.command.ContextCommand;
import net.ryanland.colossus.events.repliable.InteractionRepliableEvent;
import net.ryanland.colossus.sys.snowflake.ColossusGuild;
import net.ryanland.colossus.sys.snowflake.ColossusMember;
import net.ryanland.colossus.sys.snowflake.ColossusUser;

public final class ContextCommandEvent<T extends ISnowflake> extends BasicCommandEvent {

    private final GenericContextInteractionEvent<T> event;
    private ContextCommand<T> command;

    public ContextCommandEvent(GenericContextInteractionEvent<T> event) {
        this.event = event;
        repliableEvent = (InteractionRepliableEvent) this::getEvent;
    }

    @Override
    public GenericContextInteractionEvent<T> getEvent() {
        return event;
    }

    @Override
    public ContextCommand<T> getCommand() {
        return command;
    }

    public void setCommand(ContextCommand<T> contextCommand) {
        command = contextCommand;
    }

    /**
     * The target entity of this context interaction
     *
     * @return The target entity
     */
    public T getTarget() {
        return getEvent().getTarget();
    }

    /**
     * The target type of this context interaction
     *
     * @return The context target type
     */
    public ContextInteraction.ContextTarget getTargetType() {
        return getEvent().getTargetType();
    }

    /**
     * The {@link Interaction} instance.
     * <br>Note that this event is a delegate which implements the same interface.
     *
     * @return The {@link Interaction}
     */
    public ContextInteraction<T> getInteraction() {
        return getEvent().getInteraction();
    }

    @Override
    public String getName() {
        return getInteraction().getName();
    }

    @Override
    public JDA getJDA() {
        return getEvent().getJDA();
    }

    @Override
    public MessageChannel getChannel() {
        return getEvent().getMessageChannel();
    }

    @Override
    public boolean isFromGuild() {
        return getEvent().isFromGuild();
    }

    @Override
    public DiscordLocale getUserLocale() {
        return getEvent().getUserLocale();
    }

    @Override
    public ColossusUser getUser() {
        return new ColossusUser(event.getUser());
    }

    @Override
    public ColossusMember getMember() {
        return new ColossusMember(event.getMember());
    }

    @Override
    public ColossusGuild getGuild() {
        return new ColossusGuild(event.getGuild());
    }
}
